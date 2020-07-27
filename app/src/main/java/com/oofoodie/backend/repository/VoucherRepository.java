package com.oofoodie.backend.repository;

import com.oofoodie.backend.models.entity.Voucher;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface VoucherRepository extends ReactiveMongoRepository<Voucher, String> {

    Flux<Voucher> findAllByRestaurantId(String restaurantId);
}
