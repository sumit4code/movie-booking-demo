package com.intuit.craft.booking.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class BookingRequest implements Serializable {

    private String bookingId;
    private String eventId;
    private String subEventId;
    private List<Seat> seatNumbers;
}
