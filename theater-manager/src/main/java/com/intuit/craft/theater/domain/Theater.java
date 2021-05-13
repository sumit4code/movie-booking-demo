package com.intuit.craft.theater.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "theater")
public class Theater {

    private String id;
    private String name;
    private LocationInformation location;
    private Contact contact;
    private List<String> banners;
    private List<Screen> screen;
}
