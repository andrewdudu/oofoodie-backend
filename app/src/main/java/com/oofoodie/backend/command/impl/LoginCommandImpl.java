package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.LoginCommand;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.handler.TokenProvider;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.request.command.LoginCommandRequest;
import com.oofoodie.backend.models.response.LoginResponse;
import com.oofoodie.backend.repository.UserRepository;
import com.oofoodie.backend.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LoginCommandImpl implements LoginCommand {

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
    private GetRedisData getRedisData;

    @Override
    public Mono<ResponseEntity<?>> execute(LoginCommandRequest request) {
        return userRepository.existsByUsername(request.getUsername())
                .flatMap(user -> login(user, request));
    }

    private Mono<ResponseEntity<?>> login(Boolean isUserExist, LoginCommandRequest request) {
        if (!isUserExist)
            return Mono.error(new BadRequestException("User does not exist"));
        else {
            return getRedisData.getUser(request.getUsername())
                .flatMap(user -> {
                    if (passwordEncoder.matches(request.getPassword(), user.getPassword()) && user.getRoles().contains(request.getRole())) {
                        String accessToken = tokenProvider.generateToken(user);
                        String refreshToken = tokenProvider.generateRefreshToken(request.getUsername());
                        return Mono.just(ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(accessToken, ACCESS_TOKEN_EXPIRATION_IN_MS).toString())
                                .header(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(refreshToken, REFRESH_TOKEN_EXPIRATION_IN_MS).toString())
                                .body(new LoginResponse("Login Successfully", user)));
                    } else {
                        return Mono.error(new BadRequestException("Invalid credentials"));
                    }
                });
        }
    }
}
