package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.AddPopularRestaurantCommand;
import com.oofoodie.backend.models.entity.PopularRestaurant;
import com.oofoodie.backend.models.request.PopularRestaurantRequest;
import com.oofoodie.backend.models.response.PopularRestaurantResponse;
import com.oofoodie.backend.repository.PopularRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class AddPopularRestaurantCommandImpl implements AddPopularRestaurantCommand {

    @Autowired
    private PopularRestaurantRepository popularRestaurantRepository;

    @Override
    public Mono<PopularRestaurantResponse> execute(PopularRestaurantRequest request) {
        return Mono.fromCallable(() -> constructRequest(request))
                .flatMap(popularRestaurant -> popularRestaurantRepository.save(popularRestaurant))
                .map(this::constructResponse);
    }

    private PopularRestaurantResponse constructResponse(PopularRestaurant popularRestaurant) {
        return PopularRestaurantResponse.builder()
                .restoId(popularRestaurant.getRestoId())
                .build();
    }

    private PopularRestaurant constructRequest(PopularRestaurantRequest request) {
        return PopularRestaurant.builder()
                .restoId(request.getRestoId())
                .expiredDateInSeconds(Date.from(Instant.now().plusMillis(TimeUnit.MINUTES.toMillis(request.getExpiredDay()))))
                .build();
    }
}
