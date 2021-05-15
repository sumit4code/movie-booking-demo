package com.intuit.craft.theater.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.intuit.craft.exception.ValidationException;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collation = "event_details")
public class Event implements Serializable {

    @Id
    private String id;

    @NotNull(message = "theaterId can't be empty")
    private final String theaterId;

    @NotNull(message = "screenId can't be empty")
    private final int screenId;

    @NotNull(message = "locationId can't be empty")
    private final String locationId;

    @NotNull(message = "eventId can't be empty")
    private final String eventId;

    @NotNull(message = "eventStartDate can't be empty")
    @JsonSerialize(as = Date.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final Date eventStartDate;

    @NotNull(message = "eventEndDate can't be empty")
    @JsonSerialize(as = Date.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final Date eventEndDate;

    @NotNull(message = "eventEndDate can't be empty")
    @Builder.Default
    private final List<Schedule> schedules = new ArrayList<>();

    private List<SubEvent> subEvents;

    @CreatedDate
    @Field(name = "createdDate")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Field(name = "lastModifiedDate")
    private LocalDateTime lastModifiedDate;


    public void validate() {
        if (eventStartDate.before(eventEndDate)) {
            throw new ValidationException("eventStartDate can not be before eventEndDate");
        }

        if (!schedules.isEmpty()) {
            schedules.forEach(Schedule::validate);
        }
    }

    public Date getEventStartDate() {
        return (Date) eventStartDate.clone();
    }

    public Date getEventEndDate() {
        return (Date) eventEndDate.clone();
    }
}
