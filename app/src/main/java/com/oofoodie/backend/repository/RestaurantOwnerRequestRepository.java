package com.oofoodie.backend.repository;

import com.oofoodie.backend.models.entity.RestaurantOwnerRequest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RestaurantOwnerRequestRepository extends ReactiveMongoRepository<RestaurantOwnerRequest, String> {

    Mono<RestaurantOwnerRequest> findByMerchantUsername(String merchantUsername);

    Mono<RestaurantOwnerRequest> deleteByMerchantUsername(String merchantUsername);

    Mono<Void> deleteById(String requestId);
}
