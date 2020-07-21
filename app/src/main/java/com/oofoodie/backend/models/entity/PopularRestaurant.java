package com.oofoodie.backend.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "popular_restaurants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularRestaurant extends BaseEntity {

    private String restoId;

    @Field
    @Indexed(name = "expiredDateInSeconds", expireAfterSeconds = 60)
    private Date expiredDateInSeconds;
}
