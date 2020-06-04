package com.oofoodie.backend.repository;

import com.oofoodie.backend.models.entity.Restaurant;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends ReactiveMongoRepository<Restaurant, String> {
}
