package com.intuit.craft.booking.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubEvent implements Serializable {

    private String subEventId;
    private LocalDateTime showStartTime;
    private LocalDateTime showEndTime;
}
