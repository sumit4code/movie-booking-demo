package com.intuit.craft.booking.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "lock_state")
public class LockState implements Serializable {

    @Id
    private String id;
    private String lockId;
    private LockStatus lockstatus;
    @Field
    private LockType lockType;
    @Field
    private String transactionId;

    @CreatedDate
    @Field(name = "createdDate")
    private LocalDateTime createdDate;

    @Indexed(expireAfterSeconds = 1800)
    @LastModifiedDate
    @Field(name = "lastModifiedDate")
    private LocalDateTime lastModifiedDate;

}
