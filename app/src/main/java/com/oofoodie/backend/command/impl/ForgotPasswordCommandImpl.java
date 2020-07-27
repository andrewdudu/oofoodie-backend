package com.oofoodie.backend.command.impl;

import com.blibli.oss.command.CommandExecutor;
import com.oofoodie.backend.command.ForgotPasswordCommand;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.handler.TokenProvider;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.ForgotPasswordRequest;
import com.oofoodie.backend.models.request.MailRequest;
import com.oofoodie.backend.models.response.ForgotPasswordResponse;
import com.oofoodie.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class ForgotPasswordCommandImpl implements ForgotPasswordCommand {

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private GetRedisData getRedisData;

    @Override
    public Mono<ForgotPasswordResponse> execute(ForgotPasswordRequest request) {
        return generateResetPasswordToken(request.getEmail())
                .map(token -> {
                    MailRequest mailRequest = MailRequest.builder()
                            .email(request.getEmail())
                            .token(token)
                            .build();
                    commandExecutor.execute(MailCommandImpl.class, mailRequest)
                            .subscribeOn(Schedulers.elastic())
                            .subscribe();
                    return token;
                })
                .map(str -> constructResponse(str));
    }

    private ForgotPasswordResponse constructResponse(String str) {
        return new ForgotPasswordResponse(str);
    }

    private Mono<String> generateResetPasswordToken(String email) {
        return userRepository.findByEmail(email)
                .flatMap(user -> Mono.just(generateToken(user)))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadRequestException("User is not found"))));

    }

    private String generateToken(User user) {
        String key = "reset-password-" + user.getEmail();
        if (!redisTemplate.hasKey(key)) {
            String token = tokenProvider.generatePasswordResetToken(user.getEmail());
            getRedisData.setResetPasswordToken(key, token);

            return token;
        }

        return getRedisData.get(key);
    }
}
