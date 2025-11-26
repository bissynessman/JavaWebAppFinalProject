#define WINVER 0x0602
#define _WIN32_WINNT 0x0602
#include <windows.h>
#include <shlobj.h>
#include <commctrl.h>
#include <curl/curl.h>
#include <string>
#include <fstream>
#include <thread>
#include <chrono>
#include <sstream>
#include <iomanip>
#include <vector>
#include <openssl/x509.h>
#include <openssl/pem.h>
#include <openssl/err.h>

HWND hProgressBar;
HWND hWnd;
HWND hLabelFile;
HWND hLabelStats;
HWND hLabelPercent;
long gBandwidthLimit = 0;
std::chrono::steady_clock::time_point gStartTime;
std::string gTempFilename = "download.tmp";
std::string gFilename;

std::string get_downloads_folder() {
	PWSTR pathTmp = NULL;
	std::string pathStr;
	if (SUCCEEDED(SHGetKnownFolderPath(FOLDERID_Downloads, 0, NULL, &pathTmp))) {
		char buffer[MAX_PATH];
		wcstombs(buffer, pathTmp, MAX_PATH);
		pathStr = buffer;
		CoTaskMemFree(pathTmp);
	}
	return pathStr;
}

std::string format_bandwidth(double Bps) {
	std::ostringstream oss;
	if (Bps > 1024 * 1024) {
		oss << std::fixed << std::setprecision(2) << (Bps / (1024 * 1024)) << " MB/s";
	} else {
		oss << std::fixed << std::setprecision(1) << (Bps / 1024) << " KB/s";
	}
	return oss.str();
}

std::string format_eta(double seconds) {
	int sec = (int)seconds;
	int mins = sec / 60;
	sec = sec % 60;
	std::ostringstream oss;
	oss << mins << "m " << sec << "s";
	return oss.str();
}

std::wstring utf8_to_wstring(const std::string& str) {
	if (str.empty()) return std::wstring();
	int wstring_size = MultiByteToWideChar(CP_UTF8, 0, &str[0], (int)str.size(), NULL, 0);
	std::wstring wStr(wstring_size, 0);
	MultiByteToWideChar(CP_UTF8, 0, &str[0], (int)str.size(), &wStr[0], wstring_size);
	return wStr;
}

static std::vector<unsigned char> read_file(const char* path) {
	std::ifstream file(path, std::ios::binary);
	if (!file) return {};
	return std::vector<unsigned char>((std::istreambuf_iterator<char>(file)),
									   std::istreambuf_iterator<char>());
}

size_t header_callback(char* buffer, size_t size, size_t nitems, void* userdata) {
	size_t totalSize = size * nitems;
	std::string headerLine(buffer, totalSize);

	std::string prefix = "Content-Disposition:";
	if (headerLine.compare(0, prefix.size(), prefix) == 0) {
		std::string filenameKey = "filename=";
		size_t pos = headerLine.find(filenameKey);
		if (pos != std::string::npos) {
			pos += filenameKey.length();
			while (pos < headerLine.size() && (headerLine[pos] == ' ' || headerLine[pos] == '\"')) pos++;

			size_t endPos = headerLine.find_first_of("\"\r\n;", pos);
			if (endPos == std::string::npos)
				endPos = headerLine.size();

			gFilename = headerLine.substr(pos, endPos - pos);
			SetWindowTextW(hLabelFile, utf8_to_wstring(gFilename).c_str());
		}
	}
	return totalSize;
}

size_t write_callback(void* ptr, size_t size, size_t nmemb, void* stream) {
	std::ofstream* out = static_cast<std::ofstream*>(stream);
	out->write(static_cast<char*>(ptr), size * nmemb);
	return size * nmemb;
}

int progress_callback(void* clientp, curl_off_t dltotal, curl_off_t dlnow, curl_off_t, curl_off_t) {
	if (dltotal > 0) {
		int percent = static_cast<int>((dlnow * 100) / dltotal);
		SendMessage(hProgressBar, PBM_SETPOS, percent, 0);
		std::ostringstream percentStr;
		percentStr << percent << "%";
		SetWindowTextW(hLabelPercent, utf8_to_wstring(percentStr.str()).c_str());

		auto now = std::chrono::steady_clock::now();
		double elapsedSec = std::chrono::duration<double>(now - gStartTime).count();
		double speed = (elapsedSec > 0) ? (double) dlnow / elapsedSec : 0;
		double remainingSec = (speed > 0) ? ((double) dltotal - dlnow) / speed : 0;

		std::string stats = format_bandwidth(speed) + " - ETA: " + format_eta(remainingSec);
		SetWindowTextW(hLabelStats, utf8_to_wstring(stats).c_str());
	}
	return 0;
}

LRESULT CALLBACK WndProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam) {
	switch (msg) {
		case WM_DESTROY:
			PostQuitMessage(0);
			return 0;
	}
	return DefWindowProc(hwnd, msg, wParam, lParam);
}

void make_download_window() {
	const char CLASS_NAME[] = "ProgressWindow";
	WNDCLASSW wc = {};
	wc.lpfnWndProc = WndProc;
	wc.hInstance = GetModuleHandle(NULL);
	wc.lpszClassName = utf8_to_wstring(CLASS_NAME).c_str();
	RegisterClassW(&wc);

	hWnd = CreateWindowW(utf8_to_wstring(CLASS_NAME).c_str(), utf8_to_wstring("Downloading...").c_str(),
						 WS_OVERLAPPEDWINDOW & ~WS_MAXIMIZEBOX & ~WS_SIZEBOX,
						 CW_USEDEFAULT, CW_USEDEFAULT, 320, 150,
						 NULL, NULL, GetModuleHandle(NULL), NULL);

	InitCommonControls();

	hLabelFile = CreateWindowW(utf8_to_wstring("STATIC").c_str(), utf8_to_wstring(gFilename).c_str(),
							   WS_CHILD | WS_VISIBLE,
							   10, 10, 280, 20,
							   hWnd, NULL, GetModuleHandle(NULL), NULL);

	hProgressBar = CreateWindowEx(0, PROGRESS_CLASS, NULL,
								  WS_CHILD | WS_VISIBLE,
								  10, 40, 220, 20,
								  hWnd, NULL, GetModuleHandle(NULL), NULL);
	SendMessage(hProgressBar, PBM_SETRANGE, 0, MAKELPARAM(0, 100));

	hLabelPercent = CreateWindowW(utf8_to_wstring("STATIC").c_str(), utf8_to_wstring("0%").c_str(),
								  WS_CHILD | WS_VISIBLE | SS_RIGHT,
								  240, 40, 60, 20,
								  hWnd, NULL, GetModuleHandle(NULL), NULL);

	hLabelStats = CreateWindowW(utf8_to_wstring("STATIC").c_str(), utf8_to_wstring("0 KB/s - ETA: 0m 0s").c_str(),
								WS_CHILD | WS_VISIBLE,
								10, 70, 280, 20,
								hWnd, NULL, GetModuleHandle(NULL), NULL);

	ShowWindow(hWnd, SW_SHOW);
}

void download(std::string url) {
	std::string downloads_path = get_downloads_folder();
	std::string output_path = downloads_path + "\\" + gTempFilename;
	CURL* curl = curl_easy_init();

	if (curl) {
		std::ofstream outFile(output_path, std::ios::binary);

		gStartTime = std::chrono::steady_clock::now();

		curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
		curl_easy_setopt(curl, CURLOPT_HEADERFUNCTION, header_callback);
		curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_callback);
		curl_easy_setopt(curl, CURLOPT_WRITEDATA, &outFile);

		curl_easy_setopt(curl, CURLOPT_NOPROGRESS, 0L);
		curl_easy_setopt(curl, CURLOPT_XFERINFOFUNCTION, progress_callback);

		if (gBandwidthLimit > 0) curl_easy_setopt(curl, CURLOPT_MAX_RECV_SPEED_LARGE, (curl_off_t) gBandwidthLimit);

		curl_easy_perform(curl);
		curl_easy_cleanup(curl);

		outFile.close();

		std::string final_path = downloads_path + "\\" + gFilename;
		std::rename(output_path.c_str(), final_path.c_str());
	}

	PostMessage(hWnd, WM_CLOSE, 0, 0);
}

void showNotification(const char* message) {
	HWND hWndForeground = GetForegroundWindow();

	MessageBoxA(hWndForeground, message, "Notification", MB_OK | MB_ICONINFORMATION);
}

int verifySignature(const char* dataFilepath, const char* sigFilepath, const char* certFilepath) {
	int return_value;
	std::ifstream in(dataFilepath, std::ios::binary);
	const size_t BUF_SZ = 8192;
	std::vector<char> buffer(BUF_SZ);
	bool valid;

	ERR_load_crypto_strings();
	OpenSSL_add_all_algorithms();

	std::vector<unsigned char> sig = read_file(sigFilepath);

	if (sig.empty())
		return -1;

	BIO* bio_cert = BIO_new_file(certFilepath, "r");
	if (!bio_cert)
		return -1;

	X509* cert = PEM_read_bio_X509(bio_cert, nullptr, nullptr, nullptr);
	BIO_free(bio_cert);
	if (!cert)
		return 2;

	EVP_PKEY* pubkey = X509_get_pubkey(cert);
	X509_free(cert);
	if (!pubkey)
		return 2;

	EVP_MD_CTX* mdctx = EVP_MD_CTX_new();
	if (!mdctx) {
		return_value = 3;
		goto leave_pkey;
	}

	if (!in.is_open() || EVP_DigestVerifyInit(mdctx, nullptr, EVP_sha256(), nullptr, pubkey) != 1) {
		return_value = 4;
		goto leave_mdctx;
	}

	while (in.good()) {
		in.read(buffer.data(), (std::streamsize)BUF_SZ);
		std::streamsize nBytesRead = in.gcount();
		if (nBytesRead > 0) {
			if (EVP_DigestVerifyUpdate(mdctx, reinterpret_cast<unsigned char*>(buffer.data()), (size_t)nBytesRead) != 1) {
				return_value = 5;
				goto leave_mdctx;
			}
		}
	}
	in.close();

	valid = EVP_DigestVerifyFinal(mdctx, sig.data(), sig.size()) == 1;
	return_value = 0;

leave_mdctx:
	EVP_MD_CTX_free(mdctx);
leave_pkey:
	EVP_PKEY_free(pubkey);

	return return_value ? return_value : valid ? 0 : 1;
}

int downloadFile(const char* url, long bandwidthLimitBytesPerSec) {
	try {
		gBandwidthLimit = bandwidthLimitBytesPerSec;
		std::string urlStr(url);
		size_t pos = urlStr.find_last_of('/');

		make_download_window();

		std::thread dlThread(download, urlStr);
		dlThread.detach();

		MSG message;
		while (GetMessage(&message, NULL, 0, 0) > 0) {
			TranslateMessage(&message);
			DispatchMessage(&message);
		}

		return 0;
	} catch (...) {
		return 1;
	}
}

extern "C" __declspec(dllexport) void show_notification(const char* msg) {
	showNotification(msg);
}

extern "C" __declspec(dllexport) int verify_signature(const char* dataFilepath, const char* sigFilepath, const char* certFilepath) {
	return verifySignature(dataFilepath, sigFilepath, certFilepath);
}

extern "C" __declspec(dllexport) int download_file(const char* url, long bandwidthLimit) {
	return downloadFile(url, bandwidthLimit);
}