package com.intuit.craft.booking.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class SubEvent implements Serializable {

    private final String subEventId;
    private final LocalDateTime showStartTime;
    private final LocalDateTime showEndTime;
}
