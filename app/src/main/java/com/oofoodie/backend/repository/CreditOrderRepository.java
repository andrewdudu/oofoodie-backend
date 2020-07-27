package com.oofoodie.backend.repository;

import com.oofoodie.backend.models.entity.CreditOrder;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditOrderRepository extends ReactiveMongoRepository<CreditOrder, String> {
}
