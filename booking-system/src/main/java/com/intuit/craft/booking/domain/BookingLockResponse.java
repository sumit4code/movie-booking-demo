package com.intuit.craft.booking.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingLockResponse {

    private final String transactionId;
    private final String token;
}
