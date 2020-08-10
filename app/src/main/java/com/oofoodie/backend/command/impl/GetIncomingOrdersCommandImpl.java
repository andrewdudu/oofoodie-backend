package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.GetIncomingOrdersCommand;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.Orders;
import com.oofoodie.backend.models.entity.StatusEnum;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.response.OrdersResponse;
import com.oofoodie.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GetIncomingOrdersCommandImpl implements GetIncomingOrdersCommand {

    @Autowired
    private GetRedisData getRedisData;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Mono<List<OrdersResponse>> execute(String username) {
        return getRedisData.getUser(username)
                .flatMap(this::getAllOrders);
    }

    private Mono<List<OrdersResponse>> getAllOrders(User user) {
        return orderRepository.findAllByRestaurantIdAndStatus(user.getRestaurantOwner(), StatusEnum.ON_GOING.toString())
                .map(this::constructOrdersResponse)
                .collectList();
    }

    private OrdersResponse constructOrdersResponse(Orders orders) {
        return OrdersResponse.builder()
                .id(orders.getId())
                .orderMenu(orders.getMenu())
                .status(StatusEnum.ON_GOING.toString())
                .time(orders.getTime())
                .username(orders.getUsername())
                .build();
    }
}
