package gg.pufferfish.pufferfish.compat;

import com.google.common.io.Files;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ServerConfigurations {

    public static final String[] configurationFiles = new String[]{
        "server.properties",
        "bukkit.yml",
        "spigot.yml",
        // "paper.yml", // TODO: Figure out what to do with this.
        "pufferfish.yml",
        "jellyfish.yml"
    };

    public static Map<String, String> getCleanCopies() throws IOException {
        Map<String, String> files = new HashMap<>(configurationFiles.length);
        for (String file : configurationFiles) {
            files.put(file, getCleanCopy(file));
        }
        return files;
    }

    @SuppressWarnings("deprecation")
    public static String getCleanCopy(String configName) throws IOException {
        File file = new File(configName);

        switch (Files.getFileExtension(configName)) {
            case "properties": {
                Properties properties = new Properties();
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    properties.load(inputStream);
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                properties.store(outputStream, "");
                return Arrays.stream(outputStream.toString()
                        .split("\n"))
                    .filter(line -> !line.startsWith("#"))
                    .collect(Collectors.joining("\n"));
            }
            case "yml": {
                YamlConfiguration configuration = new YamlConfiguration();
                try {
                    configuration.load(file);
                } catch (InvalidConfigurationException e) {
                    throw new IOException(e);
                }
                return configuration.saveToString();
            }
            default:
                throw new IllegalArgumentException("Bad file type " + configName);
        }
    }

}
