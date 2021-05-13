package com.intuit.craft.cache;

import com.intuit.craft.model.CacheConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(CacheConfiguration.class)
@Slf4j
public class RedisConfig {

    @Bean
    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(CacheConfiguration cacheConfiguration) {
        return (builder) -> {
            Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
            cacheConfiguration.getTtlInSeconds().forEach((s, aLong) -> {
                log.debug("Setting ttl to {} seconds for cache with name {}", aLong, s);
                configurationMap.put(s, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(aLong)));
            });
            builder.withInitialCacheConfigurations(configurationMap);
            builder.enableStatistics();
        };
    }

}
