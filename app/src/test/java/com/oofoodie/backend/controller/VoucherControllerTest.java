package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.oofoodie.backend.BackendApplication;
import com.oofoodie.backend.command.AddVoucherCommand;
import com.oofoodie.backend.command.DeleteVoucherCommand;
import com.oofoodie.backend.command.GetAllVoucherCommand;
import com.oofoodie.backend.command.GetVoucherCommand;
import com.oofoodie.backend.models.entity.Role;
import com.oofoodie.backend.models.entity.Voucher;
import com.oofoodie.backend.models.request.VoucherRequest;
import com.oofoodie.backend.models.request.command.DeleteVoucherCommandRequest;
import com.oofoodie.backend.models.request.command.VoucherCommandRequest;
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
public class VoucherControllerTest {

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
    public void getAllVoucher() {
        when(commandExecutor.execute(GetAllVoucherCommand.class, ""))
                .thenReturn(Mono.just(Collections.singletonList(new Voucher())));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/voucher").build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(GetAllVoucherCommand.class, "");
    }

    @Test
    public void addVoucher() {
        VoucherCommandRequest request = VoucherCommandRequest.builder()
                .image("image")
                .username("username")
                .name("name")
                .build();
        VoucherRequest webRequest = VoucherRequest.builder()
                .image("image")
                .name("name")
                .build();
        when(commandExecutor.execute(AddVoucherCommand.class, request))
                .thenReturn(Mono.just(new Voucher()));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/merchant/voucher").build())
                .body(Mono.just(webRequest), VoucherRequest.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(AddVoucherCommand.class, request);
    }

    @Test
    public void getVoucher() {
        when(commandExecutor.execute(GetVoucherCommand.class, "username"))
                .thenReturn(Mono.just(Collections.singletonList(new Voucher())));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/merchant/voucher").build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(GetVoucherCommand.class, "username");
    }

    @Test
    public void deleteVoucher() {
        DeleteVoucherCommandRequest request = DeleteVoucherCommandRequest.builder()
                .username("username")
                .voucherId("id")
                .build();
        when(commandExecutor.execute(DeleteVoucherCommand.class, request))
                .thenReturn(Mono.just(true));

        webTestClient.delete()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/merchant/voucher/id").build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(DeleteVoucherCommand.class, request);
    }
}
