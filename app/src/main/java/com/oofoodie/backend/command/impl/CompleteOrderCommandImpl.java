package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.CompleteOrderCommand;
import com.oofoodie.backend.models.entity.Orders;
import com.oofoodie.backend.models.entity.StatusEnum;
import com.oofoodie.backend.models.request.command.CancelOrderCommandRequest;
import com.oofoodie.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CompleteOrderCommandImpl implements CompleteOrderCommand {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Mono<Boolean> execute(CancelOrderCommandRequest request) {
        return orderRepository.findById(request.getOrderId())
                .doOnNext(this::changeToCancel)
                .map(order -> true);
    }

    private void changeToCancel(Orders orders) {
        orders.setStatus(StatusEnum.COMPLETED.toString());
        orderRepository.save(orders)
                .subscribe();
    }
}
