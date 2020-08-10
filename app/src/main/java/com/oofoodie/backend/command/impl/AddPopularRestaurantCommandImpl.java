package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.AddPopularRestaurantCommand;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.models.entity.PopularRestaurant;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.PopularRestaurantRequest;
import com.oofoodie.backend.models.response.PopularRestaurantResponse;
import com.oofoodie.backend.repository.PopularRestaurantRepository;
import com.oofoodie.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class AddPopularRestaurantCommandImpl implements AddPopularRestaurantCommand {

    @Autowired
    private PopularRestaurantRepository popularRestaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<PopularRestaurantResponse> execute(PopularRestaurantRequest request) {
        return Mono.fromCallable(() -> constructRequest(request))
                .flatMap(popularRestaurant -> decreaseCredit(popularRestaurant, request))
                .flatMap(popularRestaurant -> popularRestaurantRepository.save(popularRestaurant))
                .map(this::constructResponse);
    }

    private Mono<PopularRestaurant> decreaseCredit(PopularRestaurant popularRestaurant, PopularRestaurantRequest request) {
        return userRepository.findByRestaurantOwner(request.getRestoId())
                .flatMap(user -> setCredit(user, request))
                .map(user -> popularRestaurant);
    }

    private Mono<User> setCredit(User user, PopularRestaurantRequest request) {
        if (user.getCredits().compareTo(request.getExpiredDay().getValue().getPrice()) < 0) {
            return Mono.error(new BadRequestException("You do not have enough credit."));
        }
        user.setCredits(user.getCredits().subtract(request.getExpiredDay().getValue().getPrice()));

        return userRepository.save(user);
    }

    private PopularRestaurantResponse constructResponse(PopularRestaurant popularRestaurant) {
        return PopularRestaurantResponse.builder()
                .restoId(popularRestaurant.getRestoId())
                .build();
    }

    private PopularRestaurant constructRequest(PopularRestaurantRequest request) {
        return PopularRestaurant.builder()
                .restoId(request.getRestoId())
                .expiredDateInSeconds(Date.from(Instant.now().plusMillis(TimeUnit.DAYS.toMillis(request.getExpiredDay().getValue().getDays()))))
                .build();
    }
}
