package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.oofoodie.backend.command.GetOrdersCommand;
import com.oofoodie.backend.command.OrderMenuCommand;
import com.oofoodie.backend.models.request.OrderRequest;
import com.oofoodie.backend.models.request.command.GetOrdersCommandRequest;
import com.oofoodie.backend.models.request.command.OrderMenuCommandRequest;
import com.oofoodie.backend.models.response.OrdersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private CommandExecutor commandExecutor;

    @PostMapping("/api/user/restaurant/{restaurantId}/order")
    public Mono<Response<Boolean>> order(@PathVariable String restaurantId,
                                         @RequestBody OrderRequest orderRequest,
                                         Authentication authentication) {
        return commandExecutor.execute(OrderMenuCommand.class, constructOrderMenuCommandRequest(restaurantId, orderRequest, authentication))
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/api/user/orders")
    public Mono<Response<List<OrdersResponse>>> getOrders(Authentication authentication) {
        return commandExecutor.execute(GetOrdersCommand.class, constructGetOrdersCommandRequest(authentication))
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    private GetOrdersCommandRequest constructGetOrdersCommandRequest(Authentication authentication) {
        return GetOrdersCommandRequest.builder()
                .username(authentication.getName())
                .build();
    }

    private OrderMenuCommandRequest constructOrderMenuCommandRequest(String restaurantId,
                                                                     OrderRequest orderRequest,
                                                                     Authentication authentication) {
        return OrderMenuCommandRequest.builder()
                .orderMenus(orderRequest.getOrderMenu())
                .restaurantId(restaurantId)
                .time(orderRequest.getTime())
                .username(authentication.getName())
                .build();
    }
}
