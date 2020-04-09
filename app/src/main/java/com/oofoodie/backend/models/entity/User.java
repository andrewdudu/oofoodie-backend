package com.oofoodie.backend.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Field(value = "name")
    private String name;

    @Field(value = "password")
    private String password;

    @Field(value = "username")
    private String username;

    @Field(value = "email")
    private String email;

    @Field(value = "likes")
    private Integer likes;

    @Field(value = "credits")
    private Integer credits;
}
