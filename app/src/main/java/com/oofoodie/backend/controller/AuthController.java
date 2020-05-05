package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.oofoodie.backend.command.impl.*;
import com.oofoodie.backend.models.entity.Role;
import com.oofoodie.backend.models.request.*;
import com.oofoodie.backend.models.response.ForgotPasswordResponse;
import com.oofoodie.backend.models.response.LoginResponse;
import com.oofoodie.backend.util.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Collections;

@RestController
public class AuthController {

    @Value("${authentication.accessTokenExpirationInMs}")
    private Long ACCESS_TOKEN_EXPIRATION_IN_MS;

    @Value("${authentication.refreshTokenExpirationInMs}")
    private Long REFRESH_TOKEN_EXPIRATION_IN_MS;

    @Autowired
    private CommandExecutor commandExecutor;

    @PostMapping("/auth/login")
    public Mono<ResponseEntity<?>> login(@RequestBody LoginRequest request) {
        return commandExecutor.execute(LoginCommandImpl.class, request)
                .subscribeOn(Schedulers.elastic());

    }

    @PostMapping("/auth/refresh")
    public Mono<ResponseEntity<?>> refresh(@CookieValue(name = "accessToken", required = false) String accessToken,
                                        @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
        RefreshRequest request = RefreshRequest.builder()
                .accessToken(decryptedAccessToken)
                .refreshToken(decryptedRefreshToken)
                .build();
        return commandExecutor.execute(RefreshTokenCommandImpl.class, request)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/auth/signup")
    public Mono<Response<LoginResponse>> signUp(@RequestBody @Validated SignupRequest request) {
        request.setRoles(new ArrayList<>(Collections.singleton(Role.ROLE_USER)));
        return commandExecutor.execute(SignupCommandImpl.class, request)
                .map(response -> ResponseHelper.ok(response))
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/auth/signup/merchant")
    public Mono<Response<LoginResponse>> signUpMerchant(@RequestBody SignupRequest request) {
        request.setRoles(new ArrayList<>(Collections.singleton(Role.ROLE_MERCHANT)));
        return commandExecutor.execute(SignupCommandImpl.class, request)
                .map(response -> ResponseHelper.ok(response))
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/auth/forgot-password")
    public Mono<Response<ForgotPasswordResponse>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return commandExecutor.execute(ForgotPasswordCommandImpl.class, request)
                .map(response -> ResponseHelper.ok(response))
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/auth/reset-password")
    public Mono<Response<String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        return commandExecutor.execute(ResetPasswordCommandImpl.class, request)
                .map(response -> ResponseHelper.ok(response))
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/api/admin/test")
    public Mono<Boolean> test() {
        return Mono.just(true);
    }
}
