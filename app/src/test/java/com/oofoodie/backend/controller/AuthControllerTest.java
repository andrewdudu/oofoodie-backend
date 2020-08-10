package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.oofoodie.backend.BackendApplication;
import com.oofoodie.backend.command.impl.*;
import com.oofoodie.backend.models.entity.Role;
import com.oofoodie.backend.models.request.*;
import com.oofoodie.backend.models.request.command.LoginCommandRequest;
import com.oofoodie.backend.models.response.ForgotPasswordResponse;
import com.oofoodie.backend.models.response.LoginResponse;
import com.oofoodie.backend.security.AuthenticationManager;
import com.oofoodie.backend.util.SecurityCipher;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BackendApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @MockBean
    private CommandExecutor commandExecutor;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${local.server.port}")
    protected int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        List<Role> authorities = Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN, Role.ROLE_MERCHANT);

        when(authenticationManager.authenticate(any())).thenReturn(Mono.just(
                new UsernamePasswordAuthenticationToken("username", "username", authorities.stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList()))));
    }

    @Test
    public void loginUser() {
        LoginCommandRequest request = LoginCommandRequest.builder()
                .username("username")
                .password("password")
                .role(Role.ROLE_USER)
                .build();
        LoginRequest webRequest = LoginRequest.builder()
                .username("username")
                .password("password")
                .build();

        when(commandExecutor.execute(LoginCommandImpl.class, request))
                .thenReturn(Mono.just(ResponseEntity.ok("OK")));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/auth/login").build())
                .body(Mono.just(webRequest), LoginRequest.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(LoginCommandImpl.class, request);
    }

    @Test
    public void loginAdmin() {
        LoginCommandRequest request = LoginCommandRequest.builder()
                .username("username")
                .password("password")
                .role(Role.ROLE_ADMIN)
                .build();
        LoginRequest webRequest = LoginRequest.builder()
                .username("username")
                .password("password")
                .build();

        when(commandExecutor.execute(LoginCommandImpl.class, request))
                .thenReturn(Mono.just(ResponseEntity.ok("OK")));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/auth/login/admin").build())
                .body(Mono.just(webRequest), LoginRequest.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(LoginCommandImpl.class, request);
    }

    @Test
    public void loginMerchant() {
        LoginCommandRequest request = LoginCommandRequest.builder()
                .username("username")
                .password("password")
                .role(Role.ROLE_MERCHANT)
                .build();
        LoginRequest webRequest = LoginRequest.builder()
                .username("username")
                .password("password")
                .build();

        when(commandExecutor.execute(LoginCommandImpl.class, request))
                .thenReturn(Mono.just(ResponseEntity.ok("OK")));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/auth/login/merchant").build())
                .body(Mono.just(webRequest), LoginRequest.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(LoginCommandImpl.class, request);
    }

    @Test
    public void logout() {
        when(commandExecutor.execute(LogoutCommandImpl.class, ""))
                .thenReturn(Mono.just(ResponseEntity.ok("OK")));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/auth/logout").build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(LogoutCommandImpl.class, "");
    }

    @Test
    public void refresh() {
        String encryptedAccessToken = SecurityCipher.encrypt("accessToken");
        String encryptedRefreshToken = SecurityCipher.encrypt("refreshToken");
        RefreshRequest request = RefreshRequest.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        when(commandExecutor.execute(RefreshTokenCommandImpl.class, request))
                .thenReturn(Mono.just(ResponseEntity.ok("OK")));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/auth/refresh").build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", encryptedAccessToken)
                .cookie("refreshToken", encryptedRefreshToken)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(RefreshTokenCommandImpl.class, request);
    }

    @Test
    public void signUp() {
        SignupRequest request = SignupRequest.builder()
                .email("email@email.com")
                .username("username")
                .name("name")
                .password("password")
                .roles(Collections.singletonList(Role.ROLE_USER))
                .build();

        when(commandExecutor.execute(SignupCommandImpl.class, request))
                .thenReturn(Mono.just(new LoginResponse()));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/auth/signup").build())
                .body(Mono.just(request), SignupRequest.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(SignupCommandImpl.class, request);
    }

    @Test
    public void signUpMerchant() {
        SignupRequest request = SignupRequest.builder()
                .email("email@email.com")
                .username("username")
                .name("name")
                .password("password")
                .roles(Collections.singletonList(Role.ROLE_MERCHANT))
                .build();

        when(commandExecutor.execute(SignupCommandImpl.class, request))
                .thenReturn(Mono.just(new LoginResponse()));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/auth/signup/merchant").build())
                .body(Mono.just(request), SignupRequest.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(SignupCommandImpl.class, request);
    }

    @Test
    public void forgotPassword() {
        ForgotPasswordRequest request = ForgotPasswordRequest.builder()
                .email("email@email.com")
                .build();

        when(commandExecutor.execute(ForgotPasswordCommandImpl.class, request))
                .thenReturn(Mono.just(new ForgotPasswordResponse()));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/auth/forgot-password").build())
                .body(Mono.just(request), ForgotPasswordRequest.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(ForgotPasswordCommandImpl.class, request);
    }

    @Test
    public void resetPassword() {
        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .token("token")
                .password("password")
                .build();

        when(commandExecutor.execute(ResetPasswordCommandImpl.class, request))
                .thenReturn(Mono.just("OK"));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/auth/reset-password").build())
                .body(Mono.just(request), ResetPasswordRequest.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(ResetPasswordCommandImpl.class, request);
    }
}
