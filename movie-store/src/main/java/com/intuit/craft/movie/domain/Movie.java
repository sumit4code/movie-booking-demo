package com.intuit.craft.movie.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "movie_store")
public class Movie implements Serializable {
    @Id
    private String id;

    @NotNull(message = "name can't be empty")
    @Field(name = "name")
    private String name;

    private MovieDetail detail;

    @CreatedDate
    @Field(name = "createdDate")
    private LocalDateTime createdDate;

    @Indexed(expireAfterSeconds = 2592000)
    @LastModifiedDate
    @Field(name = "lastModifiedDate")
    private LocalDateTime lastModifiedDate;

}
