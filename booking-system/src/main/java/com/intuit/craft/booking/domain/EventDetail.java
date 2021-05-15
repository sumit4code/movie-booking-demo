package com.intuit.craft.booking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class EventDetail {

    private final String eventId;
    private final Screen screen;
    private final List<SubEvent> subEvents;

}
