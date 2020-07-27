package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oofoodie.backend.models.entity.Role;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.repository.UserRepository;
import com.oofoodie.backend.util.SecurityCipher;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.oofoodie.backend.constants.Security.AUTHORITIES_KEY;
import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

public class ControllerTest {

    @MockBean
    protected CommandExecutor commandExecutor;

    @MockBean
    protected UserRepository userRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @Value("${local.server.port}")
    protected int port;

    private String accessToken,
            refreshToken,
            accessTokenEncrypted,
            refreshTokenEncrypted;

    @Before
    public void setUp() {
        RestAssured.port = port;

        List<Role> authorities = Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN, Role.ROLE_MERCHANT);

        this.accessToken = Jwts.builder()
                .setSubject("username")
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, "oofoodie-backend")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .compact();
        this.refreshToken = Jwts.builder()
                .setSubject("username")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(SignatureAlgorithm.HS512, "oofoodie-backend")
                .compact();

        this.accessTokenEncrypted = SecurityCipher.encrypt(accessToken);
        this.refreshTokenEncrypted = SecurityCipher.encrypt(refreshToken);

        when(userRepository.findByUsername("username"))
                .thenReturn(Mono.just(new User()));
    }

    protected RequestSpecification givenAuth(Object body) throws JsonProcessingException {
        RequestSpecification specification = given()
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", this.accessTokenEncrypted)
                .cookie("refreshToken", this.refreshTokenEncrypted);

        if (body != null) {
            specification.body(objectMapper.writeValueAsString(body));
        }

        return specification;
    }
}
