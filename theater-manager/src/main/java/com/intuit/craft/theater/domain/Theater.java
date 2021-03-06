package com.intuit.craft.theater.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@Document(collection = "theater")
public class Theater implements Serializable {

    @Id
    private String id;

    @NotNull(message = "name can't be null or empty")
    @Field("name")
    private String name;
    @NotNull(message = "location can't be null or empty")
    private LocationInformation location;
    @NotNull(message = "contact can't be null or empty")
    private Contact contact;
    private List<String> banners;
    @Builder.Default
    private List<Screen> screen = new LinkedList<>();

    @CreatedDate
    @Field(name = "createdDate")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Field(name = "lastModifiedDate")
    private LocalDateTime lastModifiedDate;
}
