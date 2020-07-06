package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.NearbyRestaurantCommand;
import com.oofoodie.backend.models.elastic.RestaurantLocation;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.NearbyRestaurantCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantLocationRepository;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class NearbyRestaurantCommandImpl implements NearbyRestaurantCommand {

    @Autowired
    private RestaurantLocationRepository restaurantLocationRepo;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Mono<List<RestaurantResponse>> execute(NearbyRestaurantCommandRequest request) {
        return searchByLocation(request)
                .flatMap(restaurant -> restaurantRepository.findById(restaurant.getRestoId()))
                .map(this::constructResponse)
                .collectList();
    }

    private Flux<RestaurantLocation> searchByLocation(NearbyRestaurantCommandRequest request) {
        QueryBuilder query = QueryBuilders
                .geoDistanceQuery("location")
                .point(request.getLat(), request.getLon())
                .distance(10, DistanceUnit.KILOMETERS);

        return Flux.fromIterable(restaurantLocationRepo.search(query));
    }

    private RestaurantResponse constructResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        BeanUtils.copyProperties(restaurant, response);

        return response;
    }
}
