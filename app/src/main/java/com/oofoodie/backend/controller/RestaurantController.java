package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.oofoodie.backend.command.SearchRestaurantCommand;
import com.oofoodie.backend.command.impl.*;
import com.oofoodie.backend.models.request.LikeRequest;
import com.oofoodie.backend.models.request.RestaurantRequest;
import com.oofoodie.backend.models.request.ReviewRequest;
import com.oofoodie.backend.models.request.command.ApprovePendingRestaurantCommandRequest;
import com.oofoodie.backend.models.request.command.BeenThereCommandRequest;
import com.oofoodie.backend.models.request.command.NearbyRestaurantCommandRequest;
import com.oofoodie.backend.models.response.LikeResponse;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.models.response.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

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

    @GetMapping("/api/restaurant/{id}")
    public Mono<Response<RestaurantResponse>> getById(@PathVariable String id) {
        return commandExecutor.execute(GetRestaurantByIdCommandImpl.class, id)
                .map(response -> ResponseHelper.ok(response))
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/api/user/restaurant/review")
    public Mono<Response<ReviewResponse>> review(@RequestHeader("restaurant-id") String id, @RequestBody ReviewRequest request, Authentication authentication) {
        request.setRestoId(id);
        request.setUser(authentication.getName());
        return commandExecutor.execute(AddReviewCommandImpl.class, request)
                .map(response -> ResponseHelper.ok(response))
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/api/user/restaurant/like")
    public Mono<Response<LikeResponse>> like(@RequestHeader("restaurant-id") String id, Authentication authentication) {
        LikeRequest request = LikeRequest.builder()
                .restoId(id)
                .username(authentication.getName())
                .build();

        return commandExecutor.execute(LikeRestaurantCommandImpl.class, request)
                .map(response -> ResponseHelper.ok(response))
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/api/user/restaurant/been-there")
    public Mono<Response<LikeResponse>> beenThere(@RequestHeader("restaurant-id") String id, Authentication authentication) {
        BeenThereCommandRequest request = BeenThereCommandRequest.builder()
                .restoId(id)
                .username(authentication.getName())
                .build();

        return commandExecutor.execute(RestaurantBeenThereCommandImpl.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/api/restaurant/nearby")
    public Mono<Response<List<RestaurantResponse>>> nearbyRestaurant(@RequestParam("lat") Double lat, @RequestParam("lon") Double lon) {
        NearbyRestaurantCommandRequest request = NearbyRestaurantCommandRequest.builder()
                .lat(lat)
                .lon(lon)
                .build();

        return commandExecutor.execute(NearbyRestaurantCommandImpl.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/api/restaurant/search")
    public Mono<Response<List<RestaurantResponse>>> searchRestaurant(@RequestParam("q") String query) {
        return commandExecutor.execute(SearchRestaurantCommand.class, query)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/api/restaurant/popular")
    public Mono<Response<PopularRestaurantResponse>> addPopularRestaurant(@RequestBody PopularRestaurantRequest request) {
        return commandExecutor.execute(AddPopularRestaurantCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/api/restaurant/popular")
    public Mono<Response<List<RestaurantResponse>>> getPopularRestaurant() {
        return commandExecutor.execute(GetPopularRestaurantCommand.class, "")
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/api/admin/restaurant")
    public Mono<Response<List<RestaurantResponse>>> getAllRestaurant() {
        return commandExecutor.execute(GetAllPendingRestaurantCommandImpl.class, "")
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/api/admin/restaurant/{restaurantId}")
    public Mono<Response<RestaurantResponse>> approvePendingRestaurant(@PathVariable String restaurantId) {
        return commandExecutor.execute(ApprovePendingRestaurantCommandImpl.class, constructApprovePendingRestaurantCommandRequest(restaurantId))
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());

    }

    private ApprovePendingRestaurantCommandRequest constructApprovePendingRestaurantCommandRequest(String restaurantId) {
        return ApprovePendingRestaurantCommandRequest.builder()
                .restaurantId(restaurantId)
                .build();
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
