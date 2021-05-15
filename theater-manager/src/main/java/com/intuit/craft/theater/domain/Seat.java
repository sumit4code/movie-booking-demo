package com.intuit.craft.theater.domain;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Seat implements Serializable, Comparable<Seat> {

    private SeatCategory category;
    private int rowNumber;
    private int seatNumber;

    @Override
    public int compareTo(Seat that) {
        return ComparisonChain.start()
                .compare(this.getCategory(), that.getCategory(), Ordering.natural().nullsLast())
                .compare(this.rowNumber, that.rowNumber)
                .compare(this.seatNumber, that.seatNumber).result();
    }
}
