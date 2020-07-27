package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.GetUserCommand;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetUserCommandImpl implements GetUserCommand {

    @Autowired
    private GetRedisData getRedisData;

    @Override
    public Mono<User> execute(String username) {
        return getRedisData.getUser(username)
                .switchIfEmpty(Mono.error(new BadRequestException("user does not exist")));
    }
}
