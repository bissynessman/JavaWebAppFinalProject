#include <openssl/x509.h>
#include <openssl/pem.h>
#include <openssl/err.h>

#include <fstream>
#include <vector>

static std::vector<unsigned char> read_file(const char* path) {
	std::ifstream file(path, std::ios::binary);
	if (!file) return {};
	return std::vector<unsigned char>((std::istreambuf_iterator<char>(file)),
									   std::istreambuf_iterator<char>());
}

int verifySignature(const char* dataFilepath, const char* sigFilepath, const char* certFilepath) {
	int return_value;
	std::ifstream in(dataFilepath, std::ios::binary);
	const size_t BUF_SZ = 8192;
	std::vector<char> buffer(BUF_SZ);
	bool valid;

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

int main(int argc, char* argv[]) {
	if (argc != 4) return 6;
	return verifySignature(argv[1], argv[2], argv[3]);
}