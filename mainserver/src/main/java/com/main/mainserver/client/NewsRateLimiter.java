package com.main.mainserver.client;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.DAYS;


@Configuration
public class NewsRateLimiter {

    private final int REQUEST_LIMIT = 100;
    private final Duration REFRESH_PERIOD = Duration.of(1, DAYS);
    private final Duration TIMEOUT_DURATION = Duration.ofMillis(1);

    @Bean("newsLimiter")
    public RateLimiter weatherLimiter() {
        RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
                .limitRefreshPeriod(REFRESH_PERIOD)
                .limitForPeriod(REQUEST_LIMIT)
                .timeoutDuration(TIMEOUT_DURATION)
                .build();
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(rateLimiterConfig);
        return rateLimiterRegistry.rateLimiter("newsLimiter");
    }

}
