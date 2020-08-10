package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.oofoodie.backend.BackendApplication;
import com.oofoodie.backend.command.GetUserCommand;
import com.oofoodie.backend.models.entity.Role;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.security.AuthenticationManager;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BackendApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @MockBean
    private CommandExecutor commandExecutor;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${local.server.port}")
    protected int port;

    protected String accessTokenEncrypted,
            refreshTokenEncrypted;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        List<Role> authorities = Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN, Role.ROLE_MERCHANT);

        when(authenticationManager.authenticate(any())).thenReturn(Mono.just(
                new UsernamePasswordAuthenticationToken("username", "username", authorities.stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList()))));
    }

    @Test
    public void getUser() {
        when(commandExecutor.execute(GetUserCommand.class, "username"))
                .thenReturn(Mono.just(new User()));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/get-user/username").build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(GetUserCommand.class, "username");
    }
}
