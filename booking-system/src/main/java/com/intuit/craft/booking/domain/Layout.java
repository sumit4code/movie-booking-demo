package com.intuit.craft.booking.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
public class Layout implements Serializable {

    private List<Seat> seats = new LinkedList<>();
}
