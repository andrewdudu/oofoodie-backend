package com.oofoodie.backend.repository;

import com.oofoodie.backend.models.entity.Orders;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<Orders, String> {

    Flux<Orders> findAllByUsername(String username);

    Flux<Orders> findAllByRestaurantIdAndStatus(String restaurantId, String status);
}
