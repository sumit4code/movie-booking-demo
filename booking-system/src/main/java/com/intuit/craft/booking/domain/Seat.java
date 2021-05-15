package com.intuit.craft.booking.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Seat implements Serializable {

    private int rowNumber;
    private int seatNumber;
}
