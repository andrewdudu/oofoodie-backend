package com.oofoodie.backend.command.impl;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.command.SignupCommand;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.SignupRequest;
import com.oofoodie.backend.models.response.LoginResponse;
import com.oofoodie.backend.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class SignupCommandImpl implements SignupCommand, Command<SignupRequest, LoginResponse> {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<LoginResponse> execute(SignupRequest request) {
        return Mono.just(request)
                .flatMap(user -> userRepository.existsByUsername(user.getUsername()))
                .flatMap(user -> saveUser(user, request));
    }

    private Mono<LoginResponse> saveUser(Boolean user, SignupRequest request) {
        if (!user) {
            User newUser = new User();
            BeanUtils.copyProperties(request, newUser);
            newUser.setId(UUID.randomUUID().toString());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            return userRepository.save(newUser)
                    .map(savedUser -> new LoginResponse("User registered successfully"));
        }

        return Mono.error(new BadRequestException("User already exists"));
    }

}