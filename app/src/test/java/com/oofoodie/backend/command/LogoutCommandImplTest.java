package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.LogoutCommandImpl;
import com.oofoodie.backend.models.response.LoginResponse;
import com.oofoodie.backend.util.CookieUtil;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutCommandImplTest {

    @InjectMocks
    private LogoutCommandImpl command;

    @Mock
    private CookieUtil cookieUtil;

    @After
    public void verifyNoInteractions() {
        verifyNoMoreInteractions(cookieUtil);
    }

    @Test
    public void executeTest() {
        String accessTokenCookie = ResponseCookie.from("accessToken", "").maxAge(0).httpOnly(true).path("/").build().toString();
        String refreshTokenCookie = ResponseCookie.from("refreshToken", "").maxAge(0).httpOnly(true).path("/").build().toString();
        when(cookieUtil.deleteAccessTokenCookie()).thenReturn(ResponseCookie.from("accessToken", "").maxAge(0).httpOnly(true).path("/").build());
        when(cookieUtil.deleteRefreshTokenCookie()).thenReturn(ResponseCookie.from("refreshToken", "").maxAge(0).httpOnly(true).path("/").build());

        StepVerifier.create(command.execute(""))
                .expectNext(ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, accessTokenCookie)
                        .header(HttpHeaders.SET_COOKIE, refreshTokenCookie)
                        .body(new LoginResponse("Logout Successfully", null)))
                .verifyComplete();

        verify(cookieUtil).deleteAccessTokenCookie();
        verify(cookieUtil).deleteRefreshTokenCookie();
    }
}
