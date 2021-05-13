package com.intuit.craft.theater.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Contact implements Serializable {

    private String email;
    private String number;
}
