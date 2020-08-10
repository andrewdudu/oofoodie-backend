package com.oofoodie.backend.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Field(value = "name")
    private String name;

    @Field(value = "roles")
    private List<Role> roles;

    @Field(value = "password")
    @JsonIgnore
    private String password;

    @Indexed
    @Field(value = "username")
    private String username;

    @Field(value = "email")
    private String email;

    @Field(value = "likes")
    private List<String> likes;

    @Field(value = "credits")
    private BigDecimal credits;

    @Field(value = "restaurant_owner")
    private String restaurantOwner;

    @Field(value = "timelines")
    private List<Timeline> timelines;

    @Field(value = "orders")
    private List<Orders> orders;

    @Field(value = "reviews")
    private List<Review> reviews;

}
