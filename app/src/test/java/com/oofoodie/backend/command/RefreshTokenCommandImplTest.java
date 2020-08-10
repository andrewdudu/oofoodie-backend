package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.RefreshTokenCommandImpl;
import com.oofoodie.backend.exception.AuthenticationFailException;
import com.oofoodie.backend.handler.TokenProvider;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.RefreshRequest;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenCommandImplTest {

    @InjectMocks
    private RefreshTokenCommandImpl command;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CookieUtil cookieUtil;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(cookieUtil, userRepository, tokenProvider);
    }

    @Test
    public void executeTestUnauthorized() {
        when(tokenProvider.validateToken("refreshToken")).thenReturn(false);

        StepVerifier.create(command.execute(RefreshRequest.builder()
                    .accessToken("accessToken")
                    .refreshToken("refreshToken")
                    .build()))
                .expectError(AuthenticationFailException.class)
                .verify();

        verify(tokenProvider).validateToken("refreshToken");
    }

    @Test
    public void executeTest() {
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

        when(tokenProvider.validateToken("refreshToken")).thenReturn(true);
        when(tokenProvider.getUsernameFromToken("refreshToken")).thenReturn("username");
        when(userRepository.findByUsername("username")).thenReturn(Mono.just(new User()));
        when(tokenProvider.generateToken(new User())).thenReturn("accessToken");
        when(cookieUtil.createAccessTokenCookie("accessToken",null)).thenReturn(accessTokenCookie);
        when(cookieUtil.createRefreshTokenCookie("refreshToken",null)).thenReturn(refreshTokenCookie);

        StepVerifier.create(command.execute(RefreshRequest.builder()
                    .accessToken("accessToken")
                    .refreshToken("refreshToken")
                    .build()))
                .expectNext(ResponseEntity.ok().contentType(APPLICATION_JSON)
                        .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                        .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                        .body(new LoginResponse("Access Token created", new User())))
                .verifyComplete();

        verify(tokenProvider).validateToken("refreshToken");
        verify(tokenProvider).getUsernameFromToken("refreshToken");
        verify(userRepository).findByUsername("username");
        verify(tokenProvider).generateToken(new User());
        verify(cookieUtil).createAccessTokenCookie("accessToken", null);
        verify(cookieUtil).createRefreshTokenCookie("refreshToken", null);
    }
}
