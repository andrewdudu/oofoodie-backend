package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.GetPopularRestaurantCommand;
import com.oofoodie.backend.helper.WebResponseConstructor;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.PopularRestaurantRepository;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GetPopularRestaurantCommandImpl implements GetPopularRestaurantCommand {

    @Autowired
    private PopularRestaurantRepository popularRestaurantRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private WebResponseConstructor webResponseConstructor;

    @Override
    public Mono<List<RestaurantResponse>> execute(String request) {
        return popularRestaurantRepository.findAll()
                .flatMap(popularRestaurant -> restaurantRepository.findByIdAndStatus(popularRestaurant.getRestoId(), true))
                .map(restaurant -> webResponseConstructor.toRestaurantResponse(restaurant))
                .collectList();
    }
}
