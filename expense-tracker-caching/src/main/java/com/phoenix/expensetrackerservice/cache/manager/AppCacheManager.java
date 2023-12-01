package com.phoenix.expensetrackerservice.cache.manager;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class AppCacheManager implements CacheManager {

    private final CaffeineCacheManager caffeineCacheManager;

    public AppCacheManager(CaffeineCacheManager caffeineCacheManager) {
        this.caffeineCacheManager = caffeineCacheManager;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = null;

        if (Objects.nonNull(caffeineCacheManager.getCache(name))) {
            cache = caffeineCacheManager.getCache(name);
        }

        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        Collection<String> cacheNames = new ArrayList<>();

        cacheNames.addAll(caffeineCacheManager.getCacheNames());

        return cacheNames;
    }
}
