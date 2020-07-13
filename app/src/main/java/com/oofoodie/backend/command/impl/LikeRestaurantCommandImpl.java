package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.LikeRestaurantCommand;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.LikeRequest;
import com.oofoodie.backend.models.response.LikeResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import com.oofoodie.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class LikeRestaurantCommandImpl implements LikeRestaurantCommand {

    @Autowired
    private GetRedisData getRedisData;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<LikeResponse> execute(LikeRequest request) {
        return restaurantRepository.findByIdAndStatus(request.getRestoId(), true)
                .flatMap(restaurant -> saveRestaurantLike(restaurant, request))
                .flatMap(user -> saveUserLike(user, request))
                .map(user -> new LikeResponse("success"));
    }

    private Mono<User> saveRestaurantLike(Restaurant restaurant, LikeRequest request) {
        List<String> restaurantLikes;
        if (restaurant.getLikes() != null) {
            restaurantLikes = restaurant.getLikes();

            if (restaurantLikes.contains(request.getUsername())) {
                restaurantLikes.remove(request.getUsername());
            } else {
                restaurantLikes.add(request.getUsername());
            }
        } else {
            restaurantLikes = new ArrayList<>();
            restaurantLikes.add(request.getUsername());
        }

        restaurant.setLikes(restaurantLikes);

        return restaurantRepository.save(restaurant)
                .flatMap(resto -> getRedisData.getUser(request.getUsername()));
    }

    private Mono<User> saveUserLike(User user, LikeRequest request) {
        List<String> userLikes;
        if (user.getLikes() != null) {
            userLikes = user.getLikes();

            if (userLikes.contains(request.getRestoId())) {
                userLikes.remove(request.getRestoId());
            } else {
                userLikes.add(request.getRestoId());
            }
        } else {
            userLikes = new ArrayList<>();
            userLikes.add(request.getRestoId());
        }

        user.setLikes(userLikes);

        return userRepository.save(user)
                .doOnNext(savedUser -> getRedisData.saveUser(savedUser));
    }
}
