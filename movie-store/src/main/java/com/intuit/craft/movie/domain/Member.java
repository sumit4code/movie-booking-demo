package com.intuit.craft.movie.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Member implements Serializable {

    private String name;
    private MemberType memberType;
    private String role;
    private String description;
}
