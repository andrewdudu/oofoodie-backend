package com.oofoodie.backend.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "restaurant_owner_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantOwnerRequest extends BaseEntity {

    @Field(name = "merchant_username")
    private String merchantUsername;

    @Field(name = "restaurant_id")
    private String restaurantId;
}
