package com.oofoodie.backend.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders extends BaseEntity {

    @Field(name = "time")
    private String time;

    @Field(name = "menu")
    private Menu menu;

    @Field(name = "user_email")
    private String userEmail;

    @Field(name = "resto_id")
    private Restaurant restaurant;

    @Field(name = "type")
    private String type;
}
