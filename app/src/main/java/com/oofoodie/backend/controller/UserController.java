package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.oofoodie.backend.command.impl.CreateUserCommandImpl;
import com.oofoodie.backend.models.request.CreateUserRequest;
import com.oofoodie.backend.models.response.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private CommandExecutor commandExecutor;

    @PostMapping("/users")
    public Mono<Response<CreateUserResponse>> createUser(@RequestBody CreateUserRequest request) {
        return commandExecutor.execute(CreateUserCommandImpl.class, request)
                .map(response -> ResponseHelper.ok(response))
                .subscribeOn(Schedulers.elastic());
    }
}
