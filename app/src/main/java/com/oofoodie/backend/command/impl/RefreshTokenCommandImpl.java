package com.oofoodie.backend.command.impl;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.command.RefreshTokenCommand;
import com.oofoodie.backend.exception.AuthenticationFailException;
import com.oofoodie.backend.handler.TokenProvider;
import com.oofoodie.backend.models.request.RefreshRequest;
import com.oofoodie.backend.models.response.LoginResponse;
import com.oofoodie.backend.repository.UserRepository;
import com.oofoodie.backend.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class RefreshTokenCommandImpl implements RefreshTokenCommand, Command<RefreshRequest, ResponseEntity<?>> {

    @Value("${authentication.accessTokenExpirationInMs}")
    private Long ACCESS_TOKEN_EXPIRATION_IN_MS;

    @Value("${authentication.refreshTokenExpirationInMs}")
    private Long REFRESH_TOKEN_EXPIRATION_IN_MS;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CookieUtil cookieUtil;

    @Override
    public Mono<ResponseEntity<?>> execute(RefreshRequest request) {
        Boolean refreshTokenValid = tokenProvider.validateToken(request.getRefreshToken());
        if (!refreshTokenValid) {
            throw new AuthenticationFailException("Refresh Token is invalid!");
        }

        String currentUsername = tokenProvider.getUsernameFromToken(request.getRefreshToken());
        return userRepository.findByUsername(currentUsername)
                .map(user -> {
                    if (user == null) {
                        return new ResponseEntity<>(new AuthenticationFailException("User not found"), HttpStatus.UNAUTHORIZED);
                    }
                    String newAccessToken = tokenProvider.generateToken(user);
                    return ResponseEntity.ok().contentType(APPLICATION_JSON)
                            .header(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(newAccessToken, ACCESS_TOKEN_EXPIRATION_IN_MS).toString())
                            .header(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(request.getRefreshToken(), REFRESH_TOKEN_EXPIRATION_IN_MS).toString())
                            .body(new LoginResponse("Access Token created"));
                });
    }

}
