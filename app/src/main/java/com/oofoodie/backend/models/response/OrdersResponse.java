package com.oofoodie.backend.models.response;

import com.oofoodie.backend.models.request.OrderMenu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersResponse {

    private String id;

    private String time;

    private List<OrderMenu> orderMenu;

    private RestaurantResponse restaurantResponse;

    private String status;

    private String username;
}
