package com.oofoodie.backend.repository;

import com.oofoodie.backend.models.entity.Restaurant;
import reactor.core.publisher.Flux;

public interface RestaurantCustomRepository {

    Flux<Restaurant> search(String term);
}
