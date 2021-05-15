package com.intuit.craft.booking.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "event_seat_mapper")
@CompoundIndexes({
        @CompoundIndex(name = "event_subEvent", def = "{'eventId' : 1, 'subEventId': 1}")
})
public class EventSeatMapper implements Serializable {

    @Id
    private String id;

    private String eventId;
    private String subEventId;
    private int seatNumber;
    private Status status;

    @CreatedDate
    @Field(name = "createdDate")
    private LocalDateTime createdDate;

    @Indexed(expireAfterSeconds = 2592000)
    @LastModifiedDate
    @Field(name = "lastModifiedDate")
    private LocalDateTime lastModifiedDate;

}
