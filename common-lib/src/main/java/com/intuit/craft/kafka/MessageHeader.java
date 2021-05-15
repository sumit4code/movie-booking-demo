package com.intuit.craft.kafka;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@Getter
@Builder
public class MessageHeader {

    private String topic;
    private String messageId;
    @Builder.Default
    private final Long timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();

    public void validate() {
        Assert.notNull(topic, "topic can't be empty or not");
        Assert.notNull(messageId, "messageId can't be empty or not");
    }
}
