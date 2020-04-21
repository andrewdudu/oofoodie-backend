package com.oofoodie.backend.command.impl;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.command.LoginCommand;
import com.oofoodie.backend.handler.TokenProvider;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.LoginRequest;
import com.oofoodie.backend.models.response.ApiResponse;
import com.oofoodie.backend.models.response.LoginResponse;
import com.oofoodie.backend.repository.UserRepository;
import com.oofoodie.backend.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoginCommandImpl implements LoginCommand, Command<LoginRequest, ResponseEntity<?>> {

    @Value("${authentication.accessTokenExpirationInMs}")
    private Long ACCESS_TOKEN_EXPIRATION_IN_MS;

    @Value("${authentication.refreshTokenExpirationInMs}")
    private Long REFRESH_TOKEN_EXPIRATION_IN_MS;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Mono<ResponseEntity<?>> execute(LoginRequest request) {
        return userRepository.existsByUsername(request.getUsername())
                .flatMap(user -> login(user, request));
    }

    private Mono<ResponseEntity<?>> login(Boolean isUserExist, LoginRequest request) {
        if (!isUserExist)
            return Mono.just(ResponseEntity.badRequest().body(new ApiResponse(400, "User does not exist", null)));
        else {
            return getUser(request.getUsername())
                .map(user -> {
                    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        // TODO: generateToken parameter

                        String accessToken = tokenProvider.generateToken(user);
                        String refreshToken = tokenProvider.generateRefreshToken(request.getUsername());
                        return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(accessToken, ACCESS_TOKEN_EXPIRATION_IN_MS).toString())
                                .header(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(refreshToken, REFRESH_TOKEN_EXPIRATION_IN_MS).toString())
                                .body(new LoginResponse("Login Successfully"));
                    } else {
                        return ResponseEntity.badRequest().body(new ApiResponse(400, "Invalid credentials", null));
                    }
                });
        }
    }

    private Mono<User> getUser(String username) {
        // check redis
        String key = "user-" + username;
        if (redisTemplate.hasKey(key)) {
            return Mono.just((User) Objects.requireNonNull(redisTemplate.opsForValue().get(key)));
        }
        return userRepository.findByUsername(username)
                .doOnNext(user -> redisTemplate.opsForValue().set(key, user, 30, TimeUnit.MINUTES));
    }

}
