package com.intuit.craft.theater.domain;

import com.intuit.craft.validator.ValidEmail;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@Builder
public class Contact implements Serializable {

    @ValidEmail
    @NotNull(message = "email can't be empty")
    private String email;

    @NotNull(message = "contact number can't be empty")
    @Pattern(regexp = "[7-9][0-9]{9}", message = "invalid mobile number")
    private String number;
}
