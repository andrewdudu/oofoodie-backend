package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.GetOrdersCommand;
import com.oofoodie.backend.helper.CalculateRatingStatsHelper;
import com.oofoodie.backend.models.entity.Orders;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.GetOrdersCommandRequest;
import com.oofoodie.backend.models.response.OrdersResponse;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.OrderRepository;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
public class GetOrdersCommandImpl implements GetOrdersCommand {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CalculateRatingStatsHelper calculateRatingStatsHelper;

    @Override
    public Mono<List<OrdersResponse>> execute(GetOrdersCommandRequest request) {
        return orderRepository.findAllByUsername(request.getUsername())
                .flatMap(this::getRestaurant)
                .collectList();
    }

    private Mono<OrdersResponse> getRestaurant(Orders orders) {
        return restaurantRepository.findById(orders.getRestaurantId())
                .map(restaurant -> constructRestaurant(restaurant, orders));
    }

    private OrdersResponse constructRestaurant(Restaurant restaurant, Orders orders) {
        RestaurantResponse restaurantResponse = new RestaurantResponse();
        if (!Objects.isNull(restaurant.getReviews())) restaurantResponse.setRatingStats(calculateRatingStatsHelper.calculateRatingStats(restaurant));
        BeanUtils.copyProperties(restaurant, restaurantResponse);

        return OrdersResponse.builder()
                .restaurantResponse(restaurantResponse)
                .orderMenu(orders.getMenu())
                .status(orders.getStatus())
                .time(orders.getTime())
                .build();
    }
}
