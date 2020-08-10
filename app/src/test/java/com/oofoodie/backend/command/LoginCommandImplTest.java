package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.LoginCommandImpl;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.handler.TokenProvider;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.Role;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.command.LoginCommandRequest;
import com.oofoodie.backend.models.response.LoginResponse;
import com.oofoodie.backend.repository.UserRepository;
import com.oofoodie.backend.util.CookieUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginCommandImplTest {

    @InjectMocks
    private LoginCommandImpl command;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private CookieUtil cookieUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GetRedisData getRedisData;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(getRedisData, userRepository, cookieUtil, tokenProvider, passwordEncoder);
    }

    @Test
    public void executeTest() {
        User user = User.builder()
                .roles(Collections.singletonList(Role.ROLE_USER))
                .password("password")
                .build();
        HttpCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "refreshToken")
                .maxAge(604800000L)
                .httpOnly(true)
                .path("/")
                .build();
        HttpCookie accessTokenCookie = ResponseCookie.from("accessToken", "accessToken")
                .maxAge(10000L)
                .httpOnly(true)
                .path("/")
                .build();

        when(userRepository.existsByUsername("username")).thenReturn(Mono.just(true));
        when(getRedisData.getUser("username")).thenReturn(Mono.just(user));
        when(passwordEncoder.matches("password", "password")).thenReturn(true);
        when(tokenProvider.generateToken(user)).thenReturn("accessToken");
        when(tokenProvider.generateRefreshToken("username")).thenReturn("refreshToken");
        when(cookieUtil.createAccessTokenCookie("accessToken",null)).thenReturn(accessTokenCookie);
        when(cookieUtil.createRefreshTokenCookie("refreshToken",null)).thenReturn(refreshTokenCookie);

        StepVerifier.create(command.execute(LoginCommandRequest.builder()
                    .role(Role.ROLE_USER)
                    .password("password")
                    .username("username")
                    .build()))
                .expectNext(ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                        .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                        .body(new LoginResponse("Login Successfully", user)))
                .verifyComplete();

        verify(userRepository).existsByUsername("username");
        verify(getRedisData).getUser("username");
        verify(passwordEncoder).matches("password", "password");
        verify(tokenProvider).generateToken(user);
        verify(tokenProvider).generateRefreshToken("username");
        verify(cookieUtil).createAccessTokenCookie("accessToken", null);
        verify(cookieUtil).createRefreshTokenCookie("refreshToken", null);
    }

    @Test
    public void executeTestPasswordMismatch() {
        User user = User.builder()
                .roles(Collections.singletonList(Role.ROLE_USER))
                .password("password")
                .build();

        when(userRepository.existsByUsername("username")).thenReturn(Mono.just(true));
        when(getRedisData.getUser("username")).thenReturn(Mono.just(user));
        when(passwordEncoder.matches("password", "password")).thenReturn(false);

        StepVerifier.create(command.execute(LoginCommandRequest.builder()
                    .role(Role.ROLE_USER)
                    .password("password")
                    .username("username")
                    .build()))
                .expectError(BadRequestException.class)
                .verify();

        verify(userRepository).existsByUsername("username");
        verify(getRedisData).getUser("username");
        verify(passwordEncoder).matches("password", "password");
    }

    @Test
    public void executeTestRoleMismatch() {
        User user = User.builder()
                .roles(Collections.singletonList(Role.ROLE_USER))
                .password("password")
                .build();

        when(userRepository.existsByUsername("username")).thenReturn(Mono.just(true));
        when(getRedisData.getUser("username")).thenReturn(Mono.just(user));
        when(passwordEncoder.matches("password", "password")).thenReturn(true);

        StepVerifier.create(command.execute(LoginCommandRequest.builder()
                    .role(Role.ROLE_ADMIN)
                    .password("password")
                    .username("username")
                    .build()))
                .expectError(BadRequestException.class)
                .verify();

        verify(userRepository).existsByUsername("username");
        verify(getRedisData).getUser("username");
        verify(passwordEncoder).matches("password", "password");
    }

    @Test
    public void executeTestRoleAndPasswordMismatch() {
        User user = User.builder()
                .roles(Collections.singletonList(Role.ROLE_USER))
                .password("password")
                .build();

        when(userRepository.existsByUsername("username")).thenReturn(Mono.just(true));
        when(getRedisData.getUser("username")).thenReturn(Mono.just(user));
        when(passwordEncoder.matches("password", "password")).thenReturn(false);

        StepVerifier.create(command.execute(LoginCommandRequest.builder()
                .role(Role.ROLE_ADMIN)
                .password("password")
                .username("username")
                .build()))
                .expectError(BadRequestException.class)
                .verify();

        verify(userRepository).existsByUsername("username");
        verify(getRedisData).getUser("username");
        verify(passwordEncoder).matches("password", "password");
    }

    @Test
    public void executeTestDoesNotExist() {
        when(userRepository.existsByUsername("username")).thenReturn(Mono.just(false));

        StepVerifier.create(command.execute(LoginCommandRequest.builder()
                    .role(Role.ROLE_USER)
                    .password("password")
                    .username("username")
                    .build()))
                .expectError(BadRequestException.class)
                .verify();

        verify(userRepository).existsByUsername("username");
    }
}
