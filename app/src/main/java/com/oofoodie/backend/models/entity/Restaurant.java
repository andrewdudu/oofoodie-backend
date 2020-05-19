package com.oofoodie.backend.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "restaurants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant extends BaseEntity {

    @Field(name = "name")
    private String name;

    @Field(name = "telephone")
    private String telephone;

    @Field(name = "location")
    private Location location;

    @Field(name = "address")
    private String address;

    @Field(name = "type")
    private String type;

    @Field(name = "cuisine")
    private String cuisine;

    @Field(name = "images")
    private List<String> images;

    @Field(name = "reviews")
    private List<Review> reviews;

    @Field(name = "menus")
    private List<Menu> menus;

    @Field(name = "likes")
    private Integer likes;

    @Field(name = "status")
    private boolean status;
}
