package com.oofoodie.backend.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
    private String password;

    @Indexed
    @Field(value = "username")
    private String username;

    @Field(value = "email")
    private String email;

    @Field(value = "likes")
    private Integer likes;

    @Field(value = "credits")
    private Integer credits;
}
