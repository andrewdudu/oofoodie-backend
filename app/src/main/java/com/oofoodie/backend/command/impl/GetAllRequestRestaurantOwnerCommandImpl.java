package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.GetAllRequestRestaurantOwnerCommand;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.entity.RestaurantOwnerRequest;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.response.RequestRestaurantOwnerResponse;
import com.oofoodie.backend.repository.RestaurantOwnerRequestRepository;
import com.oofoodie.backend.repository.RestaurantRepository;
import com.oofoodie.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GetAllRequestRestaurantOwnerCommandImpl implements GetAllRequestRestaurantOwnerCommand {

    @Autowired
    private RestaurantOwnerRequestRepository restaurantOwnerRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Mono<List<RequestRestaurantOwnerResponse>> execute(String request) {
        return restaurantOwnerRequestRepository.findAll()
                .flatMap(this::getMerchantAndRestaurant)
                .collectList();
    }

    private Mono<RequestRestaurantOwnerResponse> getMerchantAndRestaurant(RestaurantOwnerRequest restaurantOwnerRequest) {
        return Mono.zip(
                getRestaurant(restaurantOwnerRequest.getRestaurantId()),
                getMerchant(restaurantOwnerRequest.getMerchantUsername()))
                .flatMap(data -> Mono.fromCallable(() -> constructResponse(data.getT1(), data.getT2(), restaurantOwnerRequest.getId())));
    }

    private Mono<Restaurant> getRestaurant(String restaurantId) {
        return restaurantRepository.findById(restaurantId);
    }

    private Mono<User> getMerchant(String username) {
        return userRepository.findByUsername(username);
    }

    private RequestRestaurantOwnerResponse constructResponse(Restaurant restaurant, User merchant, String id) {
        return RequestRestaurantOwnerResponse.builder()
                .id(id)
                .merchant(merchant)
                .restaurant(restaurant)
                .build();
    }
}
