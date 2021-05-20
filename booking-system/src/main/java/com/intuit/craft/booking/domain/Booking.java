package com.intuit.craft.booking.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Document(collection = "event_booking_history")
public class Booking {

    @Id
    private String transactionId;
    @Indexed
    private String mobileNumber;
    private String emailId;
    private BigDecimal amount;
    private List<Seat> seats;
    private TransactionStatus status;

}
