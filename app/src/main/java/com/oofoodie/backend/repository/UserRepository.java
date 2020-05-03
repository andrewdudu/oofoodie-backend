package com.oofoodie.backend.repository;

import com.oofoodie.backend.models.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<Boolean> existsByUsername(String username);

    Mono<User> findById(String id);

    Mono<User> findByUsername(String username);
}
