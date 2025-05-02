package net.serlith.jellyfish.components.via;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IViaProvider {

    @NotNull String getProject();
    @Nullable String fetchLatestRemoteVersion();
    @Nullable String fetchLatestDownloadLink();
    void checkAndBroadcastNewVersion(@NotNull String version);
    @Nullable String fetchDownloadLink(String version);

}
