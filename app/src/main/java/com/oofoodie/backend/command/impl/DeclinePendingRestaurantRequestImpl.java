package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.DeclinePendingRestaurantRequest;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeclinePendingRestaurantRequestImpl implements DeclinePendingRestaurantRequest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Mono<Boolean> execute(String restaurantId) {
        return restaurantRepository.findByIdAndStatus(restaurantId, false)
                .switchIfEmpty(Mono.error(new BadRequestException("Restaurant not found")))
                .flatMap(restaurant -> restaurantRepository.deleteById(restaurantId))
                .map(restaurant -> true);
    }
}
