package com.intuit.craft.theater.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Seat implements Serializable,Comparable<Seat> {

    SeatCategory category;
    private int rowNumber;
    private int seatNumber;

    @Override
    public int compareTo(Seat seat) {
        ComparisonChain
        return 0;
    }
}
