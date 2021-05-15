package com.intuit.craft.booking.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Screen implements Serializable {

    private Layout layout;
    private int totalSeats;

}
