package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.ResetPasswordCommand;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.handler.TokenProvider;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.ResetPasswordRequest;
import com.oofoodie.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ResetPasswordCommandImpl implements ResetPasswordCommand {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GetRedisData getRedisData;

    @Override
    public Mono<String> execute(ResetPasswordRequest request) {
        return Mono.fromCallable(() -> tokenProvider.getEmailFromToken(request.getToken()))
                .filter(email -> redisTemplate.hasKey("reset-password-" + email))
                .flatMap(email -> userRepository.findByEmail(email))
                .doOnNext(user -> changePassword(user, request.getPassword()))
                .doOnNext(user -> userRepository.save(user))
                .doOnNext(user -> getRedisData.saveUser(user))
                .doOnNext(user -> getRedisData.deleteResetToken(user.getEmail()))
                .map(user -> "OK")
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestException("Token is invalid"))));
    }

    private User changePassword(User user, String password) {
        String newPassword = passwordEncoder.encode(password);
        user.setPassword(newPassword);

        return user;
    }
}
