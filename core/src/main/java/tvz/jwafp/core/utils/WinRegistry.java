package tvz.jwafp.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WinRegistry {
    public static final String CHANGE_LANGUAGE_REGISTRY_PATH = "HKEY_CURRENT_USER\\SOFTWARE\\NTPR\\JavaApp\\Language";
    public static final String LANGUAGE_VALUE_NAME = "lang";

    public static String readRegistryValue(String regPath, String valueName) {
        try {
            Process process = new ProcessBuilder("reg", "query", regPath, "/v", valueName)
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().startsWith(valueName)) {
                        String[] parts = line.trim().split("\\s+");
                        return parts[parts.length - 1];
                    }
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error reading registry. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean writeRegistryValue(String regPath, String valueName, String value) {
        try {
            Process process = new ProcessBuilder("reg", "add", regPath, "/v", valueName, "/t", "REG_SZ", "/d", value, "/f")
                    .redirectErrorStream(true)
                    .start();
            int exitCode = process.waitFor();

            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}
