package com.naveen.product.ratelimit;

import com.alibou.ecommerce.exception.RateLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ProductListRateLimiter {

    private final int maxRequests;
    private final long windowMillis;
    private final ConcurrentHashMap<String, CounterWindow> counters = new ConcurrentHashMap<>();

    public ProductListRateLimiter(
            @Value("${application.rate-limit.products-list.max-requests:30}") int maxRequests,
            @Value("${application.rate-limit.products-list.window-seconds:60}") long windowSeconds
    ) {
        this.maxRequests = maxRequests;
        this.windowMillis = windowSeconds * 1000;
    }

    public void validateRequest(String clientKey) {
        var now = System.currentTimeMillis();
        var counter = counters.computeIfAbsent(clientKey, key -> new CounterWindow(now));

        synchronized (counter) {
            if (now - counter.windowStartMillis >= windowMillis) {
                counter.windowStartMillis = now;
                counter.requestCount.set(0);
            }

            var currentRequests = counter.requestCount.incrementAndGet();
            if (currentRequests > maxRequests) {
                var retryAfterSeconds = Math.max(1, (windowMillis - (now - counter.windowStartMillis)) / 1000);
                throw new RateLimitExceededException("Too many requests for products list API", retryAfterSeconds);
            }
        }
    }

    private static final class CounterWindow {
        private volatile long windowStartMillis;
        private final AtomicInteger requestCount = new AtomicInteger(0);

        private CounterWindow(long windowStartMillis) {
            this.windowStartMillis = windowStartMillis;
        }
    }
}

