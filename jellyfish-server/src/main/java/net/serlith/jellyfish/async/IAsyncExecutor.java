package net.serlith.jellyfish.async;

import java.util.concurrent.CompletableFuture;

public interface IAsyncExecutor {

    CompletableFuture<Void> queue(Runnable task);
    void terminate();

}
