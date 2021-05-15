package com.intuit.craft.booking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventTask implements Serializable {

    private String eventId;
    private SubEvent subEvent;
    private Screen screen;
}
