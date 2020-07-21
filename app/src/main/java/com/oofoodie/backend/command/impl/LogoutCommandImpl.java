package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.LogoutCommand;
import com.oofoodie.backend.models.response.LoginResponse;
import com.oofoodie.backend.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LogoutCommandImpl implements LogoutCommand {

    @Autowired
    private CookieUtil cookieUtil;

    @Override
    public Mono<ResponseEntity<?>> execute(String request) {
        return Mono.fromCallable(this::constructResponse);
    }

    private ResponseEntity<LoginResponse> constructResponse() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieUtil.deleteAccessTokenCookie().toString())
                .header(HttpHeaders.SET_COOKIE, cookieUtil.deleteRefreshTokenCookie().toString())
                .body(new LoginResponse("Logout Successfully", null));
    }
}
