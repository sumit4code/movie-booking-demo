package com.intuit.craft.theater.domain;

import com.intuit.craft.exception.EntityNotFoundException;
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

    public static EventDetail from(Event event, Theater theater) {
        Screen screen = theater.getScreen().stream()
                .filter(screen1 -> screen1.getId() == event.getScreenId()).findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Screen " + event.getScreenId() + " is not present"));
        return EventDetail.builder()
                .eventId(event.getEventId())
                .subEvents(event.getSubEvents())
                .screen(screen)
                .build();
    }
}
