package net.serlith.jellyfish.components.via.providers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModrinthViaProvider implements IViaProvider {

    private static final String PROJECT_VERSIONS_API = "https://api.modrinth.com/v2/project/%s/version";

    private final Logger logger;
    private final List<JsonObject> projectVersions;

    private final String project;

    public ModrinthViaProvider(Logger logger, String project) {
        this.logger = logger;
        this.project = project;

        final String ch = JellyfishConfig.VIA.CHANNEL.name();
        final String channel = ch.equalsIgnoreCase("snapshot") ? "beta" : ch;
        List<JsonObject> pv = null;
        try {
            URI uri = new URI(String.format(PROJECT_VERSIONS_API, project));
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(uri.toURL().openStream()))) {
                Gson gson = new Gson();
                JsonArray json = gson.fromJson(reader, JsonArray.class);

                List<JsonObject> list = new ArrayList<>();
                json.forEach(element -> {
                    if (channel.equalsIgnoreCase(element.getAsJsonObject().get("version_type").getAsString())) list.add(element.getAsJsonObject());
                });
                list.sort(Comparator.comparing(i -> i.get("date_published").getAsString()));
                pv = list;

            } catch (IOException e) {
                this.logger.warn("Failed to read {} project version data in Modrinth, API might have changed", e);
            }
        } catch (URISyntaxException e) {
            this.logger.warn("Somehow the URI was not properly formated, report this as a bug", e);
        }

        this.projectVersions = pv;
    }

    public @NotNull String getProject() {
        return this.project;
    }

    public void checkAndBroadcastNewVersion(@NotNull String version) {
        String latest = this.projectVersions.getLast().get("name").getAsString();
        if (version.equalsIgnoreCase(latest)) return;
        this.logger.warn("There's a new {} version available in Modrinth! ({})", this.project, latest);
        this.logger.warn("You're running {} ({})", this.project, version);
    }

    public @Nullable String fetchLatestRemoteVersion() {
        return this.projectVersions.getLast().get("name").getAsString();
    }

    public @Nullable String fetchLatestDownloadLink() {
        JsonObject file = this.projectVersions.getLast().get("files").getAsJsonArray().asList().stream().map(JsonElement::getAsJsonObject).filter(i -> i.get("primary").getAsBoolean()).findFirst().orElse(null);
        return file == null ? null : file.get("url").getAsString();
    }

    public @Nullable String fetchDownloadLink(String version) {
        @Nullable JsonObject v = this.projectVersions.stream().map(JsonElement::getAsJsonObject).filter(i -> i.get("name").getAsString().equals(version)).findFirst().orElse(null);
        if (v == null) return null;
        @Nullable JsonObject file = v.get("files").getAsJsonArray().asList().stream().map(JsonElement::getAsJsonObject).filter(i -> i.get("primary").getAsBoolean()).findFirst().orElse(null);
        return file == null ? null : file.get("url").getAsString();
    }

}
