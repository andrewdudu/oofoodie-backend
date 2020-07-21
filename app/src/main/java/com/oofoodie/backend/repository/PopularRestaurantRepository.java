package com.oofoodie.backend.repository;

import com.oofoodie.backend.models.entity.PopularRestaurant;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PopularRestaurantRepository extends ReactiveMongoRepository<PopularRestaurant, String> {

    Mono<PopularRestaurant> findByRestoId(String restoId);
}
