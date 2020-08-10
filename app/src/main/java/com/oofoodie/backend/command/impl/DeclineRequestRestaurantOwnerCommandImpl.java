package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.DeclineRequestRestaurantOwnerCommand;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.entity.RestaurantOwnerRequest;
import com.oofoodie.backend.repository.RestaurantOwnerRequestRepository;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeclineRequestRestaurantOwnerCommandImpl implements DeclineRequestRestaurantOwnerCommand {

    @Autowired
    private RestaurantOwnerRequestRepository restaurantOwnerRequestRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Mono<Boolean> execute(String request) {
        return restaurantOwnerRequestRepository.findById(request)
                .doOnNext(restaurantOwnerRequest -> restaurantOwnerRequestRepository.deleteById(request).subscribe())
                .flatMap(this::findRestaurant)
                .map(restaurant -> true);
    }

    private Mono<Restaurant> findRestaurant(RestaurantOwnerRequest restaurantOwnerRequest) {
        return restaurantRepository.findById(restaurantOwnerRequest.getRestaurantId())
                .map(this::changeOwnerToNull)
                .flatMap(restaurant -> restaurantRepository.save(restaurant));
    }

    private Restaurant changeOwnerToNull(Restaurant restaurant) {
        restaurant.setMerchantUsername(null);
        return restaurant;
    }
}
