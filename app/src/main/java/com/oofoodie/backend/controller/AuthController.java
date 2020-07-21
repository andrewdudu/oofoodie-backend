package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.oofoodie.backend.command.impl.*;
import com.oofoodie.backend.models.entity.Role;
import com.oofoodie.backend.models.request.*;
import com.oofoodie.backend.models.request.command.LoginCommandRequest;
import com.oofoodie.backend.models.response.ForgotPasswordResponse;
import com.oofoodie.backend.models.response.LoginResponse;
import com.oofoodie.backend.util.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Collections;

@RestController
public class AuthController {

    @Autowired
    private CommandExecutor commandExecutor;

    @PostMapping("/auth/login")
    public Mono<ResponseEntity<?>> loginUser(@RequestBody LoginRequest request) {
        return commandExecutor.execute(LoginCommandImpl.class, constructLoginCommandRequest(request, Role.ROLE_USER))
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/auth/login/admin")
    public Mono<ResponseEntity<?>> loginAdmin(@RequestBody LoginRequest request) {
        return commandExecutor.execute(LoginCommandImpl.class, constructLoginCommandRequest(request, Role.ROLE_ADMIN))
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/auth/login/merchant")
    public Mono<ResponseEntity<?>> loginMerchant(@RequestBody LoginRequest request) {
        return commandExecutor.execute(LoginCommandImpl.class, constructLoginCommandRequest(request, Role.ROLE_MERCHANT))
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/auth/logout")
    public Mono<ResponseEntity<?>> logout() {
        return commandExecutor.execute(LogoutCommandImpl.class, "")
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

    private LoginCommandRequest constructLoginCommandRequest(LoginRequest loginRequest, Role role) {
        return LoginCommandRequest.builder()
                .username(loginRequest.getUsername())
                .password(loginRequest.getPassword())
                .role(role)
                .build();
    }
}
