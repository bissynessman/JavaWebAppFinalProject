package tvz.jwafp.api.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public static Boolean verifyPassword(String passwordHash, String password, String username) {
        Boolean result = false;

        String saltedPassword = salt(password, username);
        for (String pepper : PEPPERS) {
            if (passwordHash.equals(PasswordUtils.hashPassword(saltedPassword + pepper)))
                result = true;
        }

        return result;
    }

    private static String hashPassword(String spicedPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);

            byte[] hashBytes = md.digest(spicedPassword.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String salt(String password, String username) {
        StringBuilder sb = new StringBuilder();

        String salt = generateSalt(username);

        sb.append(password);
        sb.append(salt);

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
}
