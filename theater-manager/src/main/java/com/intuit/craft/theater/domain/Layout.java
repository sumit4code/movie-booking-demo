package com.intuit.craft.theater.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class Layout implements Serializable {

    List<Seat> seats;
}
