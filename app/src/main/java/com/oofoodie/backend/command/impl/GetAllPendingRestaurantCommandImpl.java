package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.GetAllPendingRestaurantCommand;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GetAllPendingRestaurantCommandImpl implements GetAllPendingRestaurantCommand {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Mono<List<RestaurantResponse>> execute(String request) {
        return restaurantRepository.findAllByStatus(false)
                        .map(this::constructResponse)
                        .collectList();
    }

    private RestaurantResponse constructResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        BeanUtils.copyProperties(restaurant, response);

        return response;
    }
}
