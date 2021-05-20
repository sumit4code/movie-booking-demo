package com.intuit.craft.booking.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BookingRequest implements Serializable {

    @NotNull
    private String transactionId;
    @NotNull
    private String mobileNumber;
    @NotNull
    private String emailId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private List<Seat> seats;
    @NotNull
    private String eventId;
    @NotNull
    private String subEventId;
}
