package com.oofoodie.backend.repository;

import com.oofoodie.backend.models.entity.Restaurant;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RestaurantRepository extends ReactiveMongoRepository<Restaurant, String> {

    Mono<Restaurant> findByIdAndStatus(String id, boolean status);

    Flux<Restaurant> findAllByStatus(Boolean status);
}
