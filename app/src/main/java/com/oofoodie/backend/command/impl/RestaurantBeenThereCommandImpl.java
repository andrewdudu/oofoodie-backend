package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.RestaurantBeenThereCommand;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.BeenThereCommandRequest;
import com.oofoodie.backend.models.response.LikeResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantBeenThereCommandImpl implements RestaurantBeenThereCommand  {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Mono<LikeResponse> execute(BeenThereCommandRequest request) {
        return restaurantRepository.findByIdAndStatus(request.getRestoId(), true)
                .flatMap(restaurant -> addBeenThere(restaurant, request))
                .map(restaurant -> new LikeResponse("success"));
    }

    private Mono<Restaurant> addBeenThere(Restaurant restaurant, BeenThereCommandRequest request) {
        List<String> visits;

        if (restaurant.getBeenThere() != null) {
            visits = restaurant.getBeenThere();

            if (!visits.contains(request.getUsername())) {
                visits.add(request.getUsername());
            } else {
                visits.remove(request.getUsername());
            }
        } else {
            visits = new ArrayList<>();
            visits.add(request.getUsername());
        }

        restaurant.setBeenThere(visits);

        return restaurantRepository.save(restaurant);
    }
}
