package com.intuit.craft.booking.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class EventTask implements Serializable {

    private final String eventId;
    private final SubEvent subEvent;
    private final Screen screen;
}
