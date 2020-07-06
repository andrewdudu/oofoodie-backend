package com.oofoodie.backend.repository.impl;

import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.repository.RestaurantCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class RestaurantRepositoryImpl implements RestaurantCustomRepository {

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<Restaurant> search(String term) {
        Query query = new Query();
        Criteria regex = Criteria.where("name").regex(".*" + term + ".*", "i");

        query.addCriteria(regex);

        return reactiveMongoTemplate.find(query, Restaurant.class);
    }


}
