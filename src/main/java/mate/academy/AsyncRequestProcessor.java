package mate.academy;

import lombok.SneakyThrows;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class AsyncRequestProcessor {
    private final Executor executor;
    private final Map<String, UserData> map = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> getUserData(userId), executor);
    }

    private UserData getUserData(String userId) {
        return map.computeIfAbsent(userId, key -> {
            // simulate data fetching
            try {
                TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(2500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            return new UserData(key, "Details for %s".formatted(key));
        });
    }
}
