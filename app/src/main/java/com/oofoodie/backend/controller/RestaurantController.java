package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.oofoodie.backend.command.impl.AddRestaurantCommandImpl;
import com.oofoodie.backend.models.request.RestaurantRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class RestaurantController {

    @Autowired
    private CommandExecutor commandExecutor;

    @PostMapping("/api/restaurant")
    public Mono<Response<RestaurantResponse>> suggestRestaurant(@RequestBody RestaurantRequest request) {
        return commandExecutor.execute(AddRestaurantCommandImpl.class, request)
                .map(response -> ResponseHelper.ok(response))
                .subscribeOn(Schedulers.elastic());
    }

//    @PostMapping("/auth/elastic")
//    public Mono<RestaurantLocation> test(@RequestBody RestaurantLocationRequest location) {
//        RestaurantLocation restoLocation = new RestaurantLocation();
//        restoLocation.setRestoId(location.getRestoId());
//        GeoPoint geoPoint = new GeoPoint(location.getLat(), location.getLng());
//        restoLocation.setLocation(geoPoint);
//        return Mono.just(restaurantLocationRepository.save(restoLocation));
//    }
//
//    @GetMapping("/auth/search")
//    public Flux<RestaurantLocation> search() {
//        QueryBuilder query = QueryBuilders
//                .geoDistanceQuery("location")
//                .point(37.7510, -97.8220)
//                .distance(10, DistanceUnit.MILES);
//        return Flux.fromIterable(restaurantLocationRepository.search(query));
//    }
}
