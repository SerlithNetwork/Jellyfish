package net.serlith.jellyfish.async;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.serlith.jellyfish.JellyfishConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsyncChunkSender implements IAsyncExecutor {

    public static AsyncChunkSender INSTANCE = new AsyncChunkSender();
    public static Logger LOGGER = LogManager.getLogger("Jellyfish Chunk Sender");

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
        1,
        JellyfishConfig.ASYNC.CHUNK_SENDING.THREADS,
        JellyfishConfig.ASYNC.CHUNK_SENDING.KEEPALIVE,
        TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(JellyfishConfig.ASYNC.CHUNK_SENDING.QUEUE_SIZE <= 0 ? JellyfishConfig.ASYNC.CHUNK_SENDING.THREADS * 512 : JellyfishConfig.ASYNC.CHUNK_SENDING.QUEUE_SIZE),
        new ThreadFactoryBuilder()
            .setNameFormat("Jellyfish Async Chunk Sender Thread - %d")
            .setPriority(Thread.MIN_PRIORITY)
            .build(),
        new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @Override
    public CompletableFuture<Void> queue(Runnable task) {
        return CompletableFuture.runAsync(task, this.executor)
            .orTimeout(60L, TimeUnit.SECONDS)
            .exceptionally(throwable -> {
                if (throwable instanceof TimeoutException e) LOGGER.warn("Chunk sending process timed out!", e);
                else LOGGER.warn("Failed to send chunk async!", throwable);
                return null;
            });
    }

    @Override
    public void terminate() {
        this.executor.shutdown();
    }

}
