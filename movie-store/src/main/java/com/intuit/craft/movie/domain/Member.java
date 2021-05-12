package com.intuit.craft.movie.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Member {

    private String name;
    private MemberType memberType;
    private String role;
    private String description;
}
