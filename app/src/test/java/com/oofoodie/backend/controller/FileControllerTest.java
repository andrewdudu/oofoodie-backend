package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.oofoodie.backend.command.impl.GetImageCommandImpl;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileControllerTest {

    @MockBean
    private CommandExecutor commandExecutor;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${local.server.port}")
    protected int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void getImage() {
        byte[] bytes = new byte[(int) 1];
        when(commandExecutor.execute(GetImageCommandImpl.class, "file.png"))
                .thenReturn(Mono.just(ResponseEntity.ok(bytes)));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/img/file.png").build())
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(GetImageCommandImpl.class, "file.png");
    }
}
