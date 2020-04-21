package com.oofoodie.backend.command.impl;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.command.CreateUserCommand;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.CreateUserRequest;
import com.oofoodie.backend.models.response.CreateUserResponse;
import com.oofoodie.backend.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CreateUserCommandImpl implements CreateUserCommand, Command<CreateUserRequest, CreateUserResponse> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<CreateUserResponse> execute(CreateUserRequest request) {
        return Mono.fromCallable(() -> createUser(request))
                .flatMap(user -> userRepository.save(user))
                .map(user -> createUserResponse(user));
    }

    private User createUser(CreateUserRequest request) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        BeanUtils.copyProperties(request, user);
        return user;
    }

    private CreateUserResponse createUserResponse(User user) {
        CreateUserResponse createUserResponse = new CreateUserResponse();
        BeanUtils.copyProperties(user, createUserResponse);
        return createUserResponse;
    }
}
