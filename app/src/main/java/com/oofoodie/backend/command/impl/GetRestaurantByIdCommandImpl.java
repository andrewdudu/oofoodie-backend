package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.GetRestaurantByIdCommand;
import com.oofoodie.backend.exception.NotFoundException;
import com.oofoodie.backend.helper.CalculateRatingStatsHelper;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class GetRestaurantByIdCommandImpl implements GetRestaurantByIdCommand {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CalculateRatingStatsHelper calculateRatingStatsHelper;

    @Override
    public Mono<RestaurantResponse> execute(String id) {
        return restaurantRepository.findByIdAndStatus(id, true)
                .map(this::constructResponse)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new NotFoundException("Restaurant not found with id " + id))));
    }

    private RestaurantResponse constructResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        if (!Objects.isNull(restaurant.getReviews())) response.setRatingStats(calculateRatingStatsHelper.calculateRatingStats(restaurant));
        BeanUtils.copyProperties(restaurant, response);

        return response;
    }

}
