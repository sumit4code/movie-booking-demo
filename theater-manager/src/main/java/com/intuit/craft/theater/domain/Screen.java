package com.intuit.craft.theater.domain;

import com.intuit.craft.exception.ValidationException;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
public class Screen implements Serializable {

    @NotNull(message = "id can't be null")
    private int id;
    @NotNull(message = "layout can't be null")
    private Layout layout;
    private int totalSeats;

    public void validate() {
        if (!(this.totalSeats == layout.getSeats().size())) {
            throw new ValidationException("seats are not mentioned properly. total seats " + totalSeats + "defined: " + layout.getSeats().size());
        }
    }

}
