package net.serlith.jellyfish.components;

import net.serlith.jellyfish.JellyfishConfig;
import net.serlith.jellyfish.components.via.IViaProvider;
import net.serlith.jellyfish.components.via.providers.HangarViaProvider;
import net.serlith.jellyfish.components.via.providers.ModrinthViaProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"ResultOfMethodCallIgnored"})
public class ViaManagerImpl extends ViaManager {

    private static final Logger LOGGER = LogManager.getLogger("ViaManager");
    private static final File DIRECTORY = new File("compat");

    private static boolean VIA_VERSION_LOADED = false;
    private static boolean VIA_BACKWARDS_LOADED = false;

    static {
        DIRECTORY.mkdirs();
    }

    public Plugin[] loadOrDownload() {
        List<Plugin> pluginList = new ArrayList<>();
        if(JellyfishConfig.VIA.VIA_VERSION.ENABLED) {
            IViaProvider provider = JellyfishConfig.VIA.PROVIDER == JellyfishConfig.VIA.Provider.MODRINTH ? new ModrinthViaProvider(LOGGER, "ViaVersion") : new HangarViaProvider(LOGGER, "ViaVersion");
            Plugin plugin = tryLoad(provider, JellyfishConfig.VIA.VIA_VERSION.VERSION);
            if (plugin != null) {
                pluginList.add(plugin);
                VIA_VERSION_LOADED = true;
            }
        }
        if(JellyfishConfig.VIA.VIA_BACKWARDS.ENABLED) {
            if (!VIA_VERSION_LOADED) {
                LOGGER.warn("ViaVersion is not loaded, will not enable ViaBackwards");
            } else {
                IViaProvider provider = JellyfishConfig.VIA.PROVIDER == JellyfishConfig.VIA.Provider.MODRINTH ? new ModrinthViaProvider(LOGGER, "ViaBackwards") : new HangarViaProvider(LOGGER, "ViaBackwards");
                Plugin plugin = tryLoad(provider, JellyfishConfig.VIA.VIA_BACKWARDS.VERSION);
                if (plugin != null) {
                    pluginList.add(plugin);
                    VIA_BACKWARDS_LOADED = true;
                }
            }
        }
        if(JellyfishConfig.VIA.VIA_REWIND.ENABLED) {
            if (!VIA_BACKWARDS_LOADED) {
                LOGGER.warn("ViaBackwards is not loaded, will not enable ViaRewind");
            } else {
                IViaProvider provider = JellyfishConfig.VIA.PROVIDER == JellyfishConfig.VIA.Provider.MODRINTH ? new ModrinthViaProvider(LOGGER, "ViaRewind") : new HangarViaProvider(LOGGER, "ViaRewind");
                Plugin plugin = tryLoad(provider, JellyfishConfig.VIA.VIA_REWIND.VERSION);
                if (plugin != null) pluginList.add(plugin);
            }
        }
        return pluginList.toArray(new Plugin[0]);
    }

    private Plugin tryLoad(@NotNull IViaProvider provider, @NotNull String version) {
        @Nullable File file = loadOrDownload(provider, version);
        if (file == null) {
            LOGGER.warn("Failed to load or download {} ({})", provider.getProject(), version);
            return null;
        }

        try {
            return Bukkit.getServer().getPluginManager().loadPlugin(file);
        } catch (InvalidPluginException | InvalidDescriptionException e) {
            LOGGER.warn("Failed to load {} {} into the server, file archive might've been corrupted", provider.getProject(), version);
            return null;
        }
    }

    private @Nullable File loadOrDownload(@NotNull IViaProvider provider, String version) {
        String project = provider.getProject();
        Path path = Paths.get(DIRECTORY.getAbsolutePath(), project, JellyfishConfig.VIA.CHANNEL.name(), version);
        try {
            Files.createDirectories(path);
            File plugin = new File(path.toFile(), project + ".jar");

            // If "1.0..." check both /project/{}/project.jar (exists) AND /project/latest/project.ver (equals)
            //    If either true: load local, if not: fetch {} and download
            // If "latest" check /project/latest/project.ver  (equals)
            //    If true: load local, if not: fetch latest and download

            if (version.equalsIgnoreCase("latest")) {
                String local = getLatestLocalVersion(project, version);
                String remote = provider.fetchLatestRemoteVersion();

                if (remote == null) {
                    LOGGER.error("Failed to load latest {} version", project);
                    return null;
                }

                if (!remote.equals(local)) {
                    String downloadString = provider.fetchLatestDownloadLink();
                    if (downloadString == null) {
                        LOGGER.error("Failed to fetch latest {} version from cloud", project);
                        return null;
                    }

                    downloadAndSave(path, project, version, remote, downloadString);
                }

                return plugin;

            } else {

                provider.checkAndBroadcastNewVersion(version);

                if (plugin.exists()) {
                    return plugin;
                }

                String local = getLatestLocalVersion(project, version);
                if (version.equalsIgnoreCase(local)) {
                    return plugin;
                }

                String downloadString = provider.fetchDownloadLink(version);
                if (downloadString == null) {
                    LOGGER.error("Failed to fetch {} ({}) from cloud", project, version);
                    return null;
                }

                Path pathLatest = Paths.get(DIRECTORY.getAbsolutePath(), project, JellyfishConfig.VIA.CHANNEL.name(), "latest");
                return new File(pathLatest.toFile(), project + ".jar");

            }

        } catch (IOException e) {
            LOGGER.error("Failed to create directory path {}", path);
            LOGGER.error("Check if you have proper OS permissions", e);
        }

        return null;
    }

    private void downloadAndSave(Path path, String project, String version, String remoteVersion, String downloadString) {
        try (BufferedInputStream in = new BufferedInputStream(new URI(downloadString).toURL().openStream())) {
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(path.toFile(), project + ".jar")))) {
                out.write(in.readAllBytes());

                if (version.equalsIgnoreCase("latest")) {
                    try (FileWriter o = new FileWriter(new File(path.toFile(), project + ".ver"))) {
                        o.write(remoteVersion);
                    } catch (IOException e) {
                        LOGGER.error("Failed to cache {} ({}) version file", project, remoteVersion, e);
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Failed to cache {} ({}) plugin file", project, remoteVersion, e);
            }
        } catch (Exception e) {
            LOGGER.error("URL for {} ({}) was somehow malformed, report this as a bug", project, remoteVersion, e);
        }
    }

    private static @Nullable String getLatestLocalVersion(@NotNull String project, @NotNull String version) {
        String local = null;
        Path path = Paths.get(DIRECTORY.getAbsolutePath(), project, JellyfishConfig.VIA.CHANNEL.name(), "latest");
        try (BufferedReader reader = new  BufferedReader(new FileReader(new File(path.toFile(), project + ".jar")))) {
            local = reader.readLine();
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            LOGGER.warn("A version file was found in /legacy/{}/{}/{}.ver but failed to read it, check if it has appropriate OS permissions", project, version, project);
        }
        return local;
    }

}
