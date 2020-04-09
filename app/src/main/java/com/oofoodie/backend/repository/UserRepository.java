package com.oofoodie.backend.repository;

import com.oofoodie.backend.models.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Optional<User> findByUsernameOrEmail(String username, String email);

    Mono<User> findById(String id);
}
