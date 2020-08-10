package com.oofoodie.backend.models.entity;

import com.oofoodie.backend.models.request.OrderMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders extends BaseEntity {

    @Field(name = "time")
    private String time;

    @Field(name = "menu")
    private List<OrderMenu> menu;

    @Field(name = "username")
    private String username;

    @Field(name = "restaurant_id")
    private String restaurantId;

    @Field(name = "status")
    private String status;
}
