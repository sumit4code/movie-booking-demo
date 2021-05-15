package com.intuit.craft.kafka;

import lombok.Getter;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;

@Getter
public class EventMessage {

    private final MessageHeader header;
    private final Object payload;

    public EventMessage(@NotNull MessageHeader header, @NotNull Object payload) {
        this.header = header;
        this.payload = payload;
        this.header.validate();
        Assert.notNull(payload, "payload can't be empty or not");
    }
}
