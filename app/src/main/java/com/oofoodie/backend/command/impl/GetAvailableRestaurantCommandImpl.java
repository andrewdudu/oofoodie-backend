package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.GetAvailableRestaurantCommand;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.GetAvailableRestaurantCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GetAvailableRestaurantCommandImpl implements GetAvailableRestaurantCommand {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Mono<List<RestaurantResponse>> execute(GetAvailableRestaurantCommandRequest request) {
        return restaurantRepository.findAllByMerchantUsernameOrMerchantUsername(request.getMerchantUsername(), null)
                .map(restaurant -> constructResponse(restaurant))
                .collectList();
    }

    private RestaurantResponse constructResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        BeanUtils.copyProperties(restaurant, response);

        return response;
    }
}
