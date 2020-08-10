package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.RequestRestaurantOwnerCommand;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.entity.RestaurantOwnerRequest;
import com.oofoodie.backend.models.request.command.RequestRestaurantOwnerCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantOwnerRequestRepository;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class RequestRestaurantOwnerCommandImpl implements RequestRestaurantOwnerCommand {

    @Autowired
    private RestaurantOwnerRequestRepository restaurantOwnerRequestRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Mono<RestaurantResponse> execute(RequestRestaurantOwnerCommandRequest request) {
        return restaurantRepository.findByMerchantUsername(request.getMerchantUsername())
                .flatMap(this::removeRequestRestaurant)
                .switchIfEmpty(requestRestaurant(request))
                .map(this::constructResponse);
    }

    private Mono<Restaurant> removeRequestRestaurant(Restaurant restaurant) {
        String merchantUsername = restaurant.getMerchantUsername();
        restaurant.setMerchantUsername(null);

        return restaurantRepository.save(restaurant)
                .doOnNext(savedRestaurant -> deleteRestaurantOwnerRequest(merchantUsername));
    }

    private void deleteRestaurantOwnerRequest(String merchantUsername) {
        restaurantOwnerRequestRepository.deleteByMerchantUsername(merchantUsername)
                .subscribe();
    }

    private Mono<Restaurant> requestRestaurant(RequestRestaurantOwnerCommandRequest request) {
        return restaurantRepository.findById(request.getRestaurantId())
                .flatMap(restaurant -> changeMerchantUsername(restaurant, request.getMerchantUsername()))
                .doOnNext(restaurant -> saveRestaurantOwnerRequest(restaurant, request.getMerchantUsername()));
    }

    private void saveRestaurantOwnerRequest(Restaurant restaurant, String merchantUsername) {
        RestaurantOwnerRequest restaurantOwnerRequest = RestaurantOwnerRequest.builder()
                .merchantUsername(merchantUsername)
                .restaurantId(restaurant.getId())
                .build();
        restaurantOwnerRequest.setId(UUID.randomUUID().toString());

        restaurantOwnerRequestRepository.save(restaurantOwnerRequest)
                .subscribe();
    }

    private Mono<Restaurant> changeMerchantUsername(Restaurant restaurant, String merchantUsername) {
        restaurant.setMerchantUsername(merchantUsername);

        return restaurantRepository.save(restaurant);
    }

    private RestaurantResponse constructResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        BeanUtils.copyProperties(restaurant, response);

        return response;
    }
}
