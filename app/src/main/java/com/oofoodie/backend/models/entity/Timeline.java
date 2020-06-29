package com.oofoodie.backend.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "timelines")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Timeline extends BaseEntity {

    @Field(value = "type")
    private String type;

    @Field(value = "resto_name")
    private Integer star;

    @Field(value = "comments")
    private String comment;

    @Field(value = "time")
    private String time;

    @Field(value = "likes")
    private Integer likes;

    @Field(value = "review_id")
    private String reviewId;

    @Field(value = "username")
    private String username;
}
