package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.oofoodie.backend.command.GetUserCommand;
import com.oofoodie.backend.models.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class UserController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping("/api/get-user/{username}")
    public Mono<Response<User>> getUser(@PathVariable String username) {
        return commandExecutor.execute(GetUserCommand.class, username)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }
}
