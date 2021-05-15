package com.intuit.craft.kafka;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "kafka")
public class TopicConfiguration {

    private List<Topic> topic = new ArrayList<>();
}
