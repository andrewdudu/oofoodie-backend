package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.oofoodie.backend.command.GetAllRequestRestaurantOwnerCommand;
import com.oofoodie.backend.models.response.RequestRestaurantOwnerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
public class RequestRestaurantOwnerController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping("/api/admin/restaurant/request")
    public Mono<Response<List<RequestRestaurantOwnerResponse>>> getRequestRestaurantOwner() {
        return commandExecutor.execute(GetAllRequestRestaurantOwnerCommand.class, "")
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }
}
