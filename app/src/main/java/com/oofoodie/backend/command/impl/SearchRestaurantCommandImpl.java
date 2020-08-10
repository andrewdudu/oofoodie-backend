package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.SearchRestaurantCommand;
import com.oofoodie.backend.helper.CalculateRatingStatsHelper;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantCustomRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
public class SearchRestaurantCommandImpl implements SearchRestaurantCommand {

    @Autowired
    private RestaurantCustomRepository restaurantRepository;

    @Autowired
    private CalculateRatingStatsHelper calculateRatingStatsHelper;

    @Override
    public Mono<List<RestaurantResponse>> execute(String term) {
        return restaurantRepository.search(term)
                .map(this::constructResponse)
                .collectList();
    }

    private RestaurantResponse constructResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        if (!Objects.isNull(restaurant.getReviews())) response.setRatingStats(calculateRatingStatsHelper.calculateRatingStats(restaurant));
        BeanUtils.copyProperties(restaurant, response);

        return response;
    }
}
