package com.intuit.craft.booking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventDetail {

    private String eventId;
    private Screen screen;
    private List<SubEvent> subEvents;

}
