package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.oofoodie.backend.BackendApplication;
import com.oofoodie.backend.command.*;
import com.oofoodie.backend.models.entity.Role;
import com.oofoodie.backend.models.request.OrderMenu;
import com.oofoodie.backend.models.request.OrderRequest;
import com.oofoodie.backend.models.request.SendEmailRequest;
import com.oofoodie.backend.models.request.command.CancelOrderCommandRequest;
import com.oofoodie.backend.models.request.command.GetOrdersCommandRequest;
import com.oofoodie.backend.models.request.command.OrderMenuCommandRequest;
import com.oofoodie.backend.models.response.OrdersResponse;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BackendApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {

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
    public void order() {
        OrderMenuCommandRequest request = OrderMenuCommandRequest.builder()
                .username("username")
                .time("time")
                .restaurantId("id")
                .orderMenus(Collections.singletonList(new OrderMenu()))
                .build();
        OrderRequest webRequest = OrderRequest.builder()
                .orderMenu(Collections.singletonList(new OrderMenu()))
                .time("time")
                .build();

        when(commandExecutor.execute(OrderMenuCommand.class, request))
                .thenReturn(Mono.just(true));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/user/restaurant/id/order").build())
                .body(Mono.just(webRequest), OrderRequest.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(OrderMenuCommand.class, request);
    }

    @Test
    public void getOrders() {
        GetOrdersCommandRequest request = GetOrdersCommandRequest.builder()
                .username("username")
                .build();

        when(commandExecutor.execute(GetOrdersCommand.class, request))
                .thenReturn(Mono.just(Collections.singletonList(new OrdersResponse())));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/user/orders").build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(GetOrdersCommand.class, request);
    }

    @Test
    public void sendEmail() {
        SendEmailRequest request = SendEmailRequest.builder()
                .email("email@email.com")
                .message("message")
                .build();

        when(commandExecutor.execute(SendEmailCommand.class, request))
                .thenReturn(Mono.just(true));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/user/send-email").build())
                .body(Mono.just(request), SendEmailRequest.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(SendEmailCommand.class, request);
    }

    @Test
    public void getIncomingOrders() {
        when(commandExecutor.execute(GetIncomingOrdersCommand.class, "username"))
                .thenReturn(Mono.just(Collections.singletonList(new OrdersResponse())));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/merchant/orders").build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(GetIncomingOrdersCommand.class, "username");
    }

    @Test
    public void cancelOrder() {
        CancelOrderCommandRequest commandRequest = CancelOrderCommandRequest.builder()
                .orderId("id")
                .username("username")
                .build();
        when(commandExecutor.execute(CancelOrderCommand.class, commandRequest))
                .thenReturn(Mono.just(true));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/merchant/orders/id/cancel").build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(CancelOrderCommand.class, commandRequest);
    }

    @Test
    public void completeOrder() {
        CancelOrderCommandRequest commandRequest = CancelOrderCommandRequest.builder()
                .orderId("id")
                .username("username")
                .build();
        when(commandExecutor.execute(CompleteOrderCommand.class, commandRequest))
                .thenReturn(Mono.just(true));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/merchant/orders/id/complete").build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(CompleteOrderCommand.class, commandRequest);
    }
}
