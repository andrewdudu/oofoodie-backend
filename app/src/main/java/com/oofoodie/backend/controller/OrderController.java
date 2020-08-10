package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.oofoodie.backend.command.*;
import com.oofoodie.backend.models.request.OrderRequest;
import com.oofoodie.backend.models.request.SendEmailRequest;
import com.oofoodie.backend.models.request.command.CancelOrderCommandRequest;
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

    @PostMapping("/api/user/send-email")
    public Mono<Response<Boolean>> sendEmail(@RequestBody SendEmailRequest request) {
        return commandExecutor.execute(SendEmailCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/api/merchant/orders")
    public Mono<Response<List<OrdersResponse>>> getIncomingOrders(Authentication authentication) {
        return commandExecutor.execute(GetIncomingOrdersCommand.class, authentication.getName())
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/api/merchant/orders/{orderId}/cancel")
    public Mono<Response<Boolean>> cancelOrder(Authentication authentication, @PathVariable String orderId) {
        return commandExecutor.execute(CancelOrderCommand.class, constructCancelOrderCommandRequest(authentication, orderId))
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/api/merchant/orders/{orderId}/complete")
    public Mono<Response<Boolean>> completeOrder(Authentication authentication, @PathVariable String orderId) {
        return commandExecutor.execute(CompleteOrderCommand.class, constructCancelOrderCommandRequest(authentication, orderId))
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    private CancelOrderCommandRequest constructCancelOrderCommandRequest(Authentication authentication, String orderId) {
        return CancelOrderCommandRequest.builder()
                .orderId(orderId)
                .username(authentication.getName())
                .build();
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
