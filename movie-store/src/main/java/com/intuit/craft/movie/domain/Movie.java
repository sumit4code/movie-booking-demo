package com.intuit.craft.movie.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "movie_store")
public class Movie {
    @Id
    private String id;

    @Field(name = "name")
    private String name;

    private MovieDetail detail;

    @CreatedDate
    @Field(name = "createdDate")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Field(name = "lastModifiedDate")
    private LocalDateTime lastModifiedDate;

}
