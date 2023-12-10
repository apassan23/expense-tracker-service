package com.phoenix.expensetrackerservice.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.phoenix.expensetrackerservice.cache.manager.AppCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Value("${cache.names}")
    private String cacheNames;

    @Value("${cache.expireAfterWrite.duration}")
    private Integer expireAfterWrite;

    @Value("${cache.expireAfterWrite.unit}")
    private TimeUnit expireAfterWriteTimeUnit;

    @Value("${cache.maximumSize}")
    private Long maximumSize;

    @Bean
    public Caffeine caffeine() {
        Objects.requireNonNull(expireAfterWrite);
        Objects.requireNonNull(expireAfterWriteTimeUnit);

        return Caffeine.newBuilder()
                .expireAfterWrite(expireAfterWrite, expireAfterWriteTimeUnit)
                .maximumSize(maximumSize);
    }

    @Bean
    public CaffeineCacheManager caffeineCacheManager(Caffeine caffeine) {
        Objects.requireNonNull(cacheNames);
        String[] caches = StringUtils.split(cacheNames, ",");
        Objects.requireNonNull(caches);
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(caches);
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }

    @Bean
    @Primary
    public AppCacheManager appCacheManager(CaffeineCacheManager caffeineCacheManager) {
        return new AppCacheManager(caffeineCacheManager);
    }
}
