package com.intuit.craft.theater.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.intuit.craft.exception.ValidationException;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;

@Data
@Builder
public class Schedule implements Serializable {

    @NotNull(message = "startTime can't be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "KK:mm")
    private LocalTime startTime;

    @NotNull(message = "endTime can't be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "KK:mm")
    private LocalTime endTime;

    public void validate() {
        if (startTime.isAfter(endTime)) {
            throw new ValidationException("endTime can't be before startTime");
        }
    }
}
