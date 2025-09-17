package tvz.jwafp.core.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

public class DigitalSignature {
    private static final String KEYSTORE_PATH = "other/keystore.p12";
    private static final String KEYSTORE_TYPE = "PKCS12";
    private static final String KEY_PASSWORD = "keypass";
    private static final String KEY_ALIAS = "jwafpkey";
    private static final String HASH_ALGORITHM = "SHA256withRSA";

    private static final String VERIFIER_PATH = "other/verifier";
    private static final String CERT_PATH = "other/cert.pem";

    public static File createDetachedSignature(File input) throws Exception {
        Signature sig = Signature.getInstance(HASH_ALGORITHM);

        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
        char[] keyPassword = KEY_PASSWORD.toCharArray();
        try (InputStream keystoreStream = DigitalSignature.class.getClassLoader().getResourceAsStream(KEYSTORE_PATH)) {
            keyStore.load(keystoreStream, keyPassword);
        }
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, keyPassword);

        sig.initSign(privateKey);

        try (InputStream in = new BufferedInputStream(new FileInputStream(input))) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) != -1) {
                sig.update(buffer, 0, read);
            }
        }

        byte[] signatureBytes = sig.sign();

        File signatureFile = new File(input.getParentFile(), input.getName() + ".p7s");
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(signatureFile))) {
            out.write(signatureBytes);
        }

        return signatureFile;
    }

    public static int verifySignature(File data, File sig) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        boolean isLinux = os.contains("linux");

        ClassPathResource verifierResource = new ClassPathResource(VERIFIER_PATH + (isLinux ? "" : ".exe"));
        ClassPathResource certResource = new ClassPathResource(CERT_PATH);
        File verifier = Files.createTempFile("verifier-", isLinux ? "" : ".exe").toFile();
        File cert = Files.createTempFile("certificate-", ".pem").toFile();

        verifier.deleteOnExit();
        try (InputStream in = verifierResource.getInputStream();
             OutputStream out = new FileOutputStream(verifier)) {
            in.transferTo(out);
        }
        verifier.setExecutable(true);

        cert.deleteOnExit();
        try (InputStream in = certResource.getInputStream();
             OutputStream out = new FileOutputStream(cert)) {
            in.transferTo(out);
        }

        ProcessBuilder pb = new ProcessBuilder(
                verifier.getAbsolutePath(), data.getAbsolutePath(), sig.getAbsolutePath(), cert.getAbsolutePath());
        pb.redirectErrorStream(true);
        Process process = pb.start();

        return process.waitFor();
    }
}
