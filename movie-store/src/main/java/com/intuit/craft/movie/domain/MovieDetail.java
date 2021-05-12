package com.intuit.craft.movie.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class MovieDetail {

    private String description;
    private ZonedDateTime releaseDate;
    private List<Member> members;


}
