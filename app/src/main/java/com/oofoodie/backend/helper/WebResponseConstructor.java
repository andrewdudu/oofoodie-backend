package com.oofoodie.backend.helper;

import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.response.RestaurantResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class WebResponseConstructor {

    @Autowired
    private CalculateRatingStatsHelper calculateRatingStatsHelper;

    public RestaurantResponse toRestaurantResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        BeanUtils.copyProperties(restaurant, response);

        if (!Objects.isNull(restaurant.getReviews()))
            response.setRatingStats(calculateRatingStatsHelper.calculateRatingStats(restaurant));

        return response;
    }
}
