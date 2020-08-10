package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.oofoodie.backend.BackendApplication;
import com.oofoodie.backend.command.EditMenuCommand;
import com.oofoodie.backend.command.GetMenuCommand;
import com.oofoodie.backend.models.entity.Menu;
import com.oofoodie.backend.models.entity.Role;
import com.oofoodie.backend.models.request.EditMenuRequest;
import com.oofoodie.backend.models.request.MenuRequest;
import com.oofoodie.backend.models.request.command.EditMenuCommandRequest;
import com.oofoodie.backend.models.request.command.GetMenuCommandRequest;
import com.oofoodie.backend.models.response.MenuResponse;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BackendApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MenuControllerTest {

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
    public void getMenu() {
        GetMenuCommandRequest request = GetMenuCommandRequest.builder()
                .restaurantId("id")
                .build();
        when(commandExecutor.execute(GetMenuCommand.class, request))
                .thenReturn(Mono.just(Collections.singletonList(new MenuResponse())));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/menu/id").build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(GetMenuCommand.class, request);
    }

    @Test
    public void editMenu() {
        EditMenuRequest webRequest = EditMenuRequest.builder()
                .menus(Collections.singletonList(MenuRequest.builder()
                        .name("name")
                        .price(BigDecimal.ONE)
                        .build()))
                .build();
        EditMenuCommandRequest request = EditMenuCommandRequest.builder()
                .restaurantId("id")
                .merchantUsername("username")
                .menu(constructMenu(webRequest.getMenus()))
                .build();

        when(commandExecutor.execute(EditMenuCommand.class, request))
                .thenReturn(Mono.just(Collections.singletonList(new MenuResponse())));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/merchant/menu/id").build())
                .body(Mono.just(webRequest), EditMenuRequest.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(EditMenuCommand.class, request);
    }

    private List<Menu> constructMenu(List<MenuRequest> menuRequests) {
        return menuRequests.stream()
                .map(menuRequest -> Menu.builder()
                        .name(menuRequest.getName())
                        .price(menuRequest.getPrice())
                        .build())
                .collect(Collectors.toList());
    }
}
