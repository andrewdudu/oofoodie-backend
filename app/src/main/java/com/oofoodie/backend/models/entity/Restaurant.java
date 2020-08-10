package com.oofoodie.backend.models.entity;

import com.oofoodie.backend.models.request.OpenHourRequest;
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
    private String image;

    @Field(name = "beenThere")
    private List<String> beenThere;

    @Field(name = "openHour")
    private OpenHourRequest openHour;

    @Field(name = "reviews")
    private List<Review> reviews;

    @Field(name = "menus")
    private List<Menu> menus;

    @Field(name = "likes")
    private List<String> likes;

    @Field(name = "status")
    private boolean status;

    @Field(name = "suggest_user")
    private String suggestUser;

    @Field(name = "merchant_username")
    private String merchantUsername;
}
