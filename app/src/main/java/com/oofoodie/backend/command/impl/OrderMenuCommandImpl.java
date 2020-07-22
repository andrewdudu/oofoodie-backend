package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.OrderMenuCommand;
import com.oofoodie.backend.models.entity.Orders;
import com.oofoodie.backend.models.entity.StatusEnum;
import com.oofoodie.backend.models.request.command.OrderMenuCommandRequest;
import com.oofoodie.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class OrderMenuCommandImpl implements OrderMenuCommand {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Mono<Boolean> execute(OrderMenuCommandRequest request) {
        return saveOrder(request)
                .map(orders -> true);
    }

    private Mono<Orders> saveOrder(OrderMenuCommandRequest request) {
        Orders orders = Orders.builder()
            .menu(request.getOrderMenus())
            .time(request.getTime())
            .restaurantId(request.getRestaurantId())
            .username(request.getUsername())
            .status(StatusEnum.ON_GOING.toString())
            .build();
        orders.setId(UUID.randomUUID().toString());

        return orderRepository.save(orders);
    }
}
