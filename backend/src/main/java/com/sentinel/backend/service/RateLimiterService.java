package com.sentinel.backend.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    // This map stores a unique bucket for every different IP address
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        // Rule: 5 requests allowed, and it refills 5 requests every 1 minute
        Refill refill = Refill.intervally(5, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(5, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    public Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, k -> createNewBucket());
    }
}