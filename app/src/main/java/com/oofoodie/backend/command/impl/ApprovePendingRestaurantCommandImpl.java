package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.ApprovePendingRestaurantCommand;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.ApprovePendingRestaurantCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ApprovePendingRestaurantCommandImpl implements ApprovePendingRestaurantCommand {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Mono<RestaurantResponse> execute(ApprovePendingRestaurantCommandRequest request) {
        return restaurantRepository.findById(request.getRestaurantId())
                .flatMap(this::changeRestaurantStatus)
                .map(this::constructResponse);
    }

    private Mono<Restaurant> changeRestaurantStatus(Restaurant restaurant) {
        restaurant.setStatus(true);

        return restaurantRepository.save(restaurant);
    }

    private RestaurantResponse constructResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        BeanUtils.copyProperties(restaurant, response);

        return response;
    }
}
