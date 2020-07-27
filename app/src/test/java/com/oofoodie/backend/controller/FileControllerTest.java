package com.oofoodie.backend.controller;

import com.oofoodie.backend.ControllerTest;
import com.oofoodie.backend.command.impl.GetImageCommandImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileControllerTest extends ControllerTest {

    @Test
    public void getImage() {
        byte[] bytes = new byte[(int) 1];
        when(commandExecutor.execute(GetImageCommandImpl.class, "file.png"))
                .thenReturn(Mono.just(ResponseEntity.ok(bytes)));

        given()
                .when()
                .get("/api/img/file.png")
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(commandExecutor).execute(GetImageCommandImpl.class, "file.png");
    }
}
