package com.oofoodie.backend.repository;

import com.oofoodie.backend.models.entity.Timeline;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimelineRepository extends ReactiveMongoRepository<Timeline, String> {
}
