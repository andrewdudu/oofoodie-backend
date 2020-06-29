package com.oofoodie.backend.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "reviews")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseEntity {

    @Field(name = "user")
    private String user;

    @Field(name = "resto_id")
    private String restoId;

    @Field(name = "star")
    private Integer star;

    @Field(name = "comment")
    private String comment;
}
