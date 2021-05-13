package com.intuit.craft.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "cache.server")
public class CacheConfiguration {

    private Map<String, Long> ttlInSeconds = new HashMap<>();
}
