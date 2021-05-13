package com.intuit.craft.theater.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Screen implements Serializable {

    private int id;
    private Layout layout;
    private int totalSeats;

}
