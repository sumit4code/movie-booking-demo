package com.intuit.craft.kafka.admin;


import com.intuit.craft.kafka.TopicConfiguration;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates required topic based on configuration provided in {@link TopicConfiguration} during start up
 */
@Configuration
@EnableConfigurationProperties(TopicConfiguration.class)
@ConditionalOnProperty(value = "kafka.topic.creation.enabled", havingValue = "true")
public class TopicInitialization implements BeanFactoryAware {
    private final TopicConfiguration topicConfiguration;
    private final String bootstrapAddress;
    private BeanFactory beanFactory;

    @Autowired
    public TopicInitialization(@Value(value = "${kafka.bootstrapAddress}") String bootstrapAddress, TopicConfiguration topicConfiguration) {
        this.topicConfiguration = topicConfiguration;
        this.bootstrapAddress = bootstrapAddress;
    }

    /**
     * On initialization it creates topic configured
     */
    @PostConstruct
    public void init() {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        topicConfiguration.getTopic().forEach(topic -> {
            NewTopic newTopic = TopicBuilder.name(topic.getName())
                    .partitions(topic.getPartitions())
                    .replicas(topic.getReplicas())
                    .build();
            configurableBeanFactory.registerSingleton(newTopic.name() + "Bean", newTopic);
        });
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

}
