package com.intuit.craft.kafka.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//@Import(value = {TopicInitialization.class, MessagePublisher.class})
@Configuration
@ConditionalOnProperty(value = "kafka.enabled", havingValue = "true")
@ComponentScan(basePackages = "com.intuit.craft.kafka")
public class EnabledKafka {
}
