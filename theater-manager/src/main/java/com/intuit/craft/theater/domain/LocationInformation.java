package com.intuit.craft.theater.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@Document(collection = "location")
public class LocationInformation implements Serializable {

    @Id
    private String id;

    @NotNull(message = "country can't be empty or null")
    @Field(name = "country")
    private String country;

    @NotNull(message = "city can't be empty or null")
    @Field(name = "city")
    private String city;
}
