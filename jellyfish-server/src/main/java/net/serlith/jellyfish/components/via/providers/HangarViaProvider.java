package net.serlith.jellyfish.components.via.providers;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import net.serlith.jellyfish.JellyfishConfig;
import net.serlith.jellyfish.components.via.IViaProvider;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class HangarViaProvider implements IViaProvider {

    private static final String PROJECT_LATEST_VERSION_API = "https://hangar.papermc.io/api/v1/projects/%s/latest?channel=%s";
    private static final String PROJECT_DOWNLOAD_API = "https://hangar.papermc.io/api/v1/projects/%s/versions/%s/PAPER/download";

    private final Logger logger;
    private final String project;

    private final String latest;

    public HangarViaProvider(Logger logger, String project) {
        this.logger = logger;
        this.project = project;

        String version = null;
        try {
            URI uri = new URI(String.format(PROJECT_LATEST_VERSION_API, project, JellyfishConfig.VIA.CHANNEL.name()));
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(uri.toURL().openStream()))) {
                Gson gson = new Gson();
                JsonPrimitive json = gson.fromJson(reader, JsonPrimitive.class);

                version = json.getAsString();

            } catch (IOException e) {
                this.logger.warn("Failed to read {} project version data in Modrinth, API might have changed", e);
            }
        } catch (URISyntaxException e) {
            this.logger.warn("Somehow the URI was not properly formated, report this as a bug", e);
        }

        this.latest = version;
    }

    @Override
    public @NotNull String getProject() {
        return this.project;
    }

    @Override
    public void checkAndBroadcastNewVersion(@NotNull String version) {
        if (version.equalsIgnoreCase(this.latest)) return;
        this.logger.warn("There's a new {} version available in Hangar! ({})", this.project, this.latest);
        this.logger.warn("You're running {} ({})", this.project, version);
    }

    @Override
    public @Nullable String fetchLatestRemoteVersion() {
        return this.latest;
    }

    @Override
    public @Nullable String fetchLatestDownloadLink() {
        return String.format(PROJECT_DOWNLOAD_API, this.project, this.latest);
    }

    @Override
    public @Nullable String fetchDownloadLink(String version) {
        return String.format(PROJECT_DOWNLOAD_API, this.project, version);
    }

}
