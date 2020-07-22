package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.GetMenuCommand;
import com.oofoodie.backend.models.entity.Menu;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.GetMenuCommandRequest;
import com.oofoodie.backend.models.response.MenuResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GetMenuCommandImpl implements GetMenuCommand {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Mono<List<MenuResponse>> execute(GetMenuCommandRequest request) {
        return restaurantRepository.findById(request.getRestaurantId())
                .map(restaurant -> constructResponse(restaurant));
    }

    private List<MenuResponse> constructResponse(Restaurant restaurant) {
        List<Menu> menus = restaurant.getMenus();

        if (Objects.isNull(menus)) return new ArrayList<>();
        else return constructMenuResponse(menus);
    }

    private List<MenuResponse> constructMenuResponse(List<Menu> menus) {
        return menus.stream()
                .map(menu -> MenuResponse.builder()
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .build())
                .collect(Collectors.toList());
    }
}
