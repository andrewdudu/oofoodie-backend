package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.EditMenuCommand;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.models.entity.Menu;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.EditMenuCommandRequest;
import com.oofoodie.backend.models.response.MenuResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EditMenuCommandImpl implements EditMenuCommand {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Mono<List<MenuResponse>> execute(EditMenuCommandRequest request) {
        return restaurantRepository.findById(request.getRestaurantId())
                .flatMap(restaurant -> checkIfUsernameMatch(restaurant, request))
                .flatMap(restaurant -> editMenu(restaurant, request))
                .map(this::constructResponse)
                .switchIfEmpty(Mono.error(new BadRequestException("Restaurant is not owned by this Merchant")));
    }

    private Mono<Restaurant> checkIfUsernameMatch(Restaurant restaurant, EditMenuCommandRequest request) {
        if (restaurant.getMerchantUsername().equals(request.getMerchantUsername())) return Mono.just(restaurant);

        return Mono.empty();
    }

    private Mono<Restaurant> editMenu(Restaurant restaurant, EditMenuCommandRequest request) {
        restaurant.setMenus(request.getMenu());

        return restaurantRepository.save(restaurant);
    }

    private List<MenuResponse> constructResponse(Restaurant restaurant) {
        return restaurant.getMenus().stream()
                .map(this::constructMenuResponse)
                .collect(Collectors.toList());
    }

    private MenuResponse constructMenuResponse(Menu menu) {
        return MenuResponse.builder()
                .name(menu.getName())
                .price(menu.getPrice())
                .build();
    }
}
