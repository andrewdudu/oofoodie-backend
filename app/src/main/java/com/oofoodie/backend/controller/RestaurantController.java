package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.oofoodie.backend.command.*;
import com.oofoodie.backend.command.impl.*;
import com.oofoodie.backend.models.request.*;
import com.oofoodie.backend.models.request.command.*;
import com.oofoodie.backend.models.response.LikeResponse;
import com.oofoodie.backend.models.response.PopularRestaurantResponse;
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

    @PostMapping("/api/user/restaurant")
    public Mono<Response<RestaurantResponse>> suggestRestaurant(@RequestBody RestaurantRequest request, Authentication authentication) {
        return commandExecutor.execute(AddRestaurantCommandImpl.class, constructAddRestaurantCommandRequest(request, authentication))
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

    @PostMapping("/api/merchant/restaurant/popular")
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

    @PostMapping("/api/admin/restaurant/{requestId}/request")
    public Mono<Response<Boolean>> approveRequestRestaurantOwner(@PathVariable String requestId) {
        return commandExecutor.execute(ApproveRequestRestaurantOwnerCommand.class, constructApproveRequestRestaurantOwnerCommandRequest(requestId))
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/api/merchant/restaurant/available")
    public Mono<Response<List<RestaurantResponse>>> getAvailableRestaurant(Authentication authentication) {
        return commandExecutor.execute(GetAvailableRestaurantCommand.class,
                constructGetAvailableRestaurantCommandRequest(authentication))
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/api/merchant/restaurant/{restaurantId}/request")
    public Mono<Response<RestaurantResponse>> requestRestaurant(@PathVariable String restaurantId, Authentication authentication) {
        return commandExecutor.execute(RequestRestaurantOwnerCommand.class, constructRequestRestaurantOwnerCommandRequest(restaurantId, authentication))
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    private ApproveRequestRestaurantOwnerCommandRequest constructApproveRequestRestaurantOwnerCommandRequest(String requestId) {
        return ApproveRequestRestaurantOwnerCommandRequest.builder()
                .requestId(requestId)
                .build();
    }

    private RequestRestaurantOwnerCommandRequest constructRequestRestaurantOwnerCommandRequest(String restaurantId, Authentication authentication) {
        return RequestRestaurantOwnerCommandRequest.builder()
                .merchantUsername(authentication.getName())
                .restaurantId(restaurantId)
                .build();
    }

    private GetAvailableRestaurantCommandRequest constructGetAvailableRestaurantCommandRequest(Authentication authentication) {
        return GetAvailableRestaurantCommandRequest.builder()
                .merchantUsername(authentication.getName())
                .build();
    }

    private ApprovePendingRestaurantCommandRequest constructApprovePendingRestaurantCommandRequest(String restaurantId) {
        return ApprovePendingRestaurantCommandRequest.builder()
                .restaurantId(restaurantId)
                .build();
    }

    private AddRestaurantCommandRequest constructAddRestaurantCommandRequest(RestaurantRequest restaurantRequest, Authentication authentication) {
        return AddRestaurantCommandRequest.builder()
                .name(restaurantRequest.getName())
                .telephone(restaurantRequest.getTelephone())
                .location(restaurantRequest.getLocation())
                .address(restaurantRequest.getAddress())
                .type(restaurantRequest.getType())
                .cuisine(restaurantRequest.getCuisine())
                .image(restaurantRequest.getImage())
                .openHour(restaurantRequest.getOpenHour())
                .suggestUser(authentication.getName())
                .build();
    }
}
