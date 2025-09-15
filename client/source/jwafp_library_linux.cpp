#include <gtk/gtk.h>
#include <curl/curl.h>
#include <openssl/x509.h>
#include <openssl/pem.h>
#include <openssl/err.h>

#include <string>
#include <fstream>
#include <thread>
#include <chrono>
#include <sstream>
#include <iomanip>
#include <vector>

GtkWidget* gProgressBar = nullptr;
GtkWidget* gLabelFile = nullptr;
GtkWidget* gLabelStats = nullptr;
GtkWidget* gLabelPercent = nullptr;

long gBandwidthLimit = 0;
std::chrono::steady_clock::time_point gStartTime;
std::string gTempFilename = "download.tmp";
std::string gFilename;
int gDownloadResult;

std::string get_downloads_folder() {
	const char* xdg = getenv("XDG_DOWNLOAD_DIR");
	if (xdg && *xdg)
		return std::string(xdg);
	const char* home = getenv("HOME");
	return std::string(home) + "/Downloads";
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

			std::string* filenameCopy = new std::string(gFilename);
			g_idle_add([](void* data) -> gboolean {
				std::string* fname = static_cast<std::string*>(data);
				gtk_label_set_text(GTK_LABEL(gLabelFile), fname->c_str());
				delete fname;
				return G_SOURCE_REMOVE;
			}, filenameCopy);
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
	struct ProgressData {
		int percent;
		std::string stats;
	};
	
	if (dltotal > 0) {
		int percent = static_cast<int>((dlnow * 100) / dltotal);
		auto now = std::chrono::steady_clock::now();
		double sElapsed = std::chrono::duration<double>(now - gStartTime).count();
		double speed = (sElapsed > 0) ? (double)dlnow / sElapsed : 0;
		double sRemaining = (speed > 0) ? ((double)dltotal - dlnow) / speed : 0;

		std::string stats = format_bandwidth(speed) + " - ETA: " + format_eta(sRemaining);
		ProgressData* progress = new ProgressData{ percent, stats };

		g_idle_add([](void* data) -> gboolean {
			ProgressData* progress = static_cast<ProgressData*>(data);
			char percentText[16];
			snprintf(percentText, sizeof(percentText), "%d%%", progress->percent);

			gtk_progress_bar_set_fraction(GTK_PROGRESS_BAR(gProgressBar), progress->percent / 100.0);
			gtk_label_set_text(GTK_LABEL(gLabelPercent), percentText);
			gtk_label_set_text(GTK_LABEL(gLabelStats), progress->stats.c_str());

			delete progress;
			return G_SOURCE_REMOVE;
		}, progress);
	}
	return 0;
}

void download(std::string url) {
	std::string downloads_path = get_downloads_folder();
	std::string output_path = downloads_path + "/" + gTempFilename;

	CURL* curl = curl_easy_init();
	if (!curl) return;

	std::ofstream outFile(output_path, std::ios::binary);
	gStartTime = std::chrono::steady_clock::now();

	curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
	curl_easy_setopt(curl, CURLOPT_HEADERFUNCTION, header_callback);
	curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_callback);
	curl_easy_setopt(curl, CURLOPT_WRITEDATA, &outFile);

	curl_easy_setopt(curl, CURLOPT_NOPROGRESS, 0L);
	curl_easy_setopt(curl, CURLOPT_XFERINFOFUNCTION, progress_callback);

	if (gBandwidthLimit > 0) curl_easy_setopt(curl, CURLOPT_MAX_RECV_SPEED_LARGE, (curl_off_t) gBandwidthLimit);

	CURLcode res = curl_easy_perform(curl);
	if(res == CURLE_OK) {
		std::string final_path = downloads_path + "/" + gFilename;
		std::rename(output_path.c_str(), final_path.c_str());
		gDownloadResult = 0;
	} else
		gDownloadResult = 1;

	g_idle_add([](void*) -> gboolean { gtk_main_quit(); return G_SOURCE_REMOVE; }, nullptr);
	curl_easy_cleanup(curl);
	outFile.close();
}

void showNotification(const char* message) {
	GtkWidget* dialog = gtk_message_dialog_new(
		nullptr,
		GTK_DIALOG_MODAL,
		GTK_MESSAGE_INFO,
		GTK_BUTTONS_OK,
		"%s",
		message
	);

	gtk_widget_show_all(dialog);
	gtk_dialog_run(GTK_DIALOG(dialog));
	gtk_widget_destroy(dialog);
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

void make_download_window() {
	GtkWidget* window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	gtk_window_set_title(GTK_WINDOW(window), "Downloading...");
	gtk_window_set_default_size(GTK_WINDOW(window), 320, 150);
	gtk_window_set_resizable(GTK_WINDOW(window), FALSE);
	gtk_window_set_type_hint(GTK_WINDOW(window), GDK_WINDOW_TYPE_HINT_DIALOG);

	GtkWidget* outer_vbox = gtk_box_new(GTK_ORIENTATION_VERTICAL, 0);
	gtk_container_add(GTK_CONTAINER(window), outer_vbox);

	GtkWidget* top_spacer = gtk_box_new(GTK_ORIENTATION_VERTICAL, 0);
	gtk_box_pack_start(GTK_BOX(outer_vbox), top_spacer, TRUE, TRUE, 0);

	GtkWidget* vbox = gtk_box_new(GTK_ORIENTATION_VERTICAL, 5);
	gtk_container_add(GTK_CONTAINER(outer_vbox), vbox);

	gLabelFile = gtk_label_new("File: unknown");
	gtk_box_pack_start(GTK_BOX(vbox), gLabelFile, FALSE, FALSE, 0);

	gProgressBar = gtk_progress_bar_new();
	gtk_box_pack_start(GTK_BOX(vbox), gProgressBar, FALSE, FALSE, 0);

	gLabelPercent = gtk_label_new("0%");
	gtk_box_pack_start(GTK_BOX(vbox), gLabelPercent, FALSE, FALSE, 0);

	gLabelStats = gtk_label_new("0 KB/s - ETA: 0m 0s");
	gtk_box_pack_start(GTK_BOX(vbox), gLabelStats, FALSE, FALSE, 0);

	GtkWidget* bottom_spacer = gtk_box_new(GTK_ORIENTATION_VERTICAL, 0);
	gtk_box_pack_start(GTK_BOX(outer_vbox), bottom_spacer, TRUE, TRUE, 0);

	gtk_widget_show_all(window);
}

int downloadFile(const char* url, long bandwidthLimitBytesPerSec) {
	gBandwidthLimit = bandwidthLimitBytesPerSec;
	std::string urlStr(url);
	size_t pos = urlStr.find_last_of('/');

	make_download_window();

	std::thread dlThread(download, urlStr);
	dlThread.detach();

	gtk_main();
	return gDownloadResult;
}

void initializeGtk() {
	int argc = 0;
	char** argv = nullptr;
	gtk_init(&argc, &argv);
}

extern "C" __attribute__((visibility("default"))) void show_notification(const char* msg) {
	initializeGtk();
	showNotification(msg);
}

extern "C" __attribute__((visibility("default"))) int verify_signature(const char* dataFilepath, const char* sigFilepath, const char* certFilepath) {
	return verifySignature(dataFilepath, sigFilepath, certFilepath);
}

extern "C" __attribute__((visibility("default"))) int download_file(const char* url, long bandwidthLimit) {
	initializeGtk();
	return downloadFile(url, bandwidthLimit);
}