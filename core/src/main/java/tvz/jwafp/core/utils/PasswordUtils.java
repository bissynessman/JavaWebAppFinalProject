package tvz.jwafp.core.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class PasswordUtils {
    private static final String ALGORITHM = "SHA-256";
    private static final String[] PEPPERS = {
            "_B2sv:b8",
            "4utJctyv",
            "-sBXN)GL",
            "Gg:MM4GJ",
            "/@s?sv)/",
            "(ITBwy*7",
            "E.1PWWaQ",
            "Z9_ftZ-o"
    };

    public static String hashPassword(String password, String username) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);

            String spiced = spice(password, username);
            byte[] hashBytes = md.digest(spiced.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String spice(String password, String username) {
        StringBuilder sb = new StringBuilder();

        String salt = generateSalt(username);
        String pepper = PEPPERS[generatePepperIndex()];

        sb.append(password);
        sb.append(salt);
        sb.append(pepper);

        return sb.toString();
    }

    private static String generateSalt(String username) {
        StringBuilder sb = new StringBuilder();

        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);

            for (int i = 0; i < username.length() - 1; i++) {
                byte[] hashBytes = md.digest(username.substring(i, i + 2).getBytes(StandardCharsets.UTF_8));

                for (int j = 0; j < hashBytes.length; j += 4) {
                    int value = 0;
                    for (int k = 0; k < 4 && (j + k) < hashBytes.length; k++)
                        value |= (hashBytes[j + k] & 0xFF) << (8 * (3 - k));

                    int modValue = value % 361;
                    if (modValue < 0) modValue += 361;

                    sb.append(Integer.toString(modValue, 36));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }

    private static int generatePepperIndex() {
        Random rng = new Random();
        return rng.nextInt(PEPPERS.length);
    }
}
