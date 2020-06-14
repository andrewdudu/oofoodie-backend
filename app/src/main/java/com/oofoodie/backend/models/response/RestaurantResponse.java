package com.oofoodie.backend.models.response;

import com.oofoodie.backend.models.entity.Location;
import com.oofoodie.backend.models.entity.Menu;
import com.oofoodie.backend.models.entity.Orders;
import com.oofoodie.backend.models.entity.Review;
import com.oofoodie.backend.models.request.OpenHourRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {

    private String name;

    private String telephone;

    private Location location;

    private String address;

    private String type;

    private String cuisine;

    private OpenHourRequest openHour;

    private String image;

    private List<Review> reviews;

    private List<Menu> menus;

    private List<Orders> orders;

    private Integer likes;

    private String status;
}
