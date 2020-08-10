package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.CancelOrderCommand;
import com.oofoodie.backend.models.entity.Orders;
import com.oofoodie.backend.models.entity.StatusEnum;
import com.oofoodie.backend.models.request.command.CancelOrderCommandRequest;
import com.oofoodie.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CancelOrderCommandImpl implements CancelOrderCommand {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Mono<Boolean> execute(CancelOrderCommandRequest request) {
        return orderRepository.findById(request.getOrderId())
                .doOnNext(this::changeToCancel)
                .map(order -> true);
    }

    private void changeToCancel(Orders orders) {
        orders.setStatus(StatusEnum.CANCELLED.toString());
        orderRepository.save(orders)
                .subscribe();
    }
}
