package com.intuit.craft.movie.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class MovieDetail implements Serializable {

    private String description;
    @NotNull(message = "releaseDate can't be empty")
    private ZonedDateTime releaseDate;
    private List<Member> members;
    private List<String> bannerLocations;
    private List<String> promoLinks;
    @NotNull
    private MovieType movieType;
    @NotNull
    private Language language;

}
