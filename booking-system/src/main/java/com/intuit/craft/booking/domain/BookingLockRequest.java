package com.intuit.craft.booking.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class BookingLockRequest implements Serializable {

    private String eventId;
    private String subEventId;
    private List<Seat> seatNumbers;
}
