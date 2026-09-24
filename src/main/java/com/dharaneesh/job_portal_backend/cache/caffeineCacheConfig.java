package com.dharaneesh.job_portal_backend.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.cache.autoconfigure.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
public class caffeineCacheConfig {

    @Bean
    public CacheManager caffeineCacheManager() {

        CaffeineCache jobsCache=new CaffeineCache("jobs",
                Caffeine.newBuilder()
                        .expireAfterWrite(10, TimeUnit.MINUTES)
                        .maximumSize(5000)
                        .build());
        CaffeineCache companyCache=new CaffeineCache("companies",
                Caffeine.newBuilder()
                        .expireAfterWrite(10,TimeUnit.MINUTES)
                        .maximumSize(500)
                        .build());
        CaffeineCache roleCache=new CaffeineCache("roles",
                Caffeine.newBuilder()
                        .expireAfterWrite(1,TimeUnit.DAYS)
                        .maximumSize(100)
                        .build());
        SimpleCacheManager simpleCacheManager=new SimpleCacheManager();
        simpleCacheManager.setCaches(Arrays.asList(jobsCache,companyCache,roleCache));
        return simpleCacheManager;
    }
}
