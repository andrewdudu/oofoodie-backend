package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.oofoodie.backend.BackendApplication;
import com.oofoodie.backend.command.*;
import com.oofoodie.backend.command.impl.*;
import com.oofoodie.backend.models.entity.DurationEnum;
import com.oofoodie.backend.models.entity.Location;
import com.oofoodie.backend.models.entity.Role;
import com.oofoodie.backend.models.request.*;
import com.oofoodie.backend.models.request.command.*;
import com.oofoodie.backend.models.response.LikeResponse;
import com.oofoodie.backend.models.response.PopularRestaurantResponse;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.models.response.ReviewResponse;
import com.oofoodie.backend.security.AuthenticationManager;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BackendApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerTest {

    @MockBean
    private CommandExecutor commandExecutor;

    @Captor
    private ArgumentCaptor<AddRestaurantCommandRequest> addRestaurantCommandRequestArgumentCaptor;

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
    public void suggestRestaurant() throws JsonProcessingException {
        Hour hour = Hour.builder().close("close").open("open").build();
        RestaurantRequest restaurantRequest = RestaurantRequest.builder()
                .address("address")
                .cuisine("cuisine")
                .image("image")
                .location(new Location((float) 0, (float) 0))
                .name("name")
                .openHour(OpenHourRequest.builder()
                        .wednesday(hour)
                        .tuesday(hour)
                        .thursday(hour)
                        .sunday(hour)
                        .saturday(hour)
                        .monday(hour)
                        .friday(hour)
                        .build())
                .telephone("telephone")
                .type("type")
                .build();

        when(commandExecutor.execute(eq(AddRestaurantCommandImpl.class), any(AddRestaurantCommandRequest.class)))
                .thenReturn(Mono.just(new RestaurantResponse()));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/user/restaurant").build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .body(Mono.just(restaurantRequest), RestaurantRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(eq(AddRestaurantCommandImpl.class), addRestaurantCommandRequestArgumentCaptor.capture());
    }

    @Test
    public void getById() {
        when(commandExecutor.execute(GetRestaurantByIdCommandImpl.class, "id"))
                .thenReturn(Mono.just(new RestaurantResponse()));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/restaurant/id").build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(GetRestaurantByIdCommandImpl.class, "id");
    }

    @Test
    public void review() {
        ReviewRequest reviewRequest = ReviewRequest.builder()
                .user("username")
                .restoId("id")
                .star(5)
                .comment("comment")
                .build();

        when(commandExecutor.execute(AddReviewCommandImpl.class, reviewRequest))
                .thenReturn(Mono.just(new ReviewResponse()));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/user/restaurant/review").build())
                .body(Mono.just(reviewRequest), ReviewRequest.class)
                .header("restaurant-id", "id")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(AddReviewCommandImpl.class, reviewRequest);
    }

    @Test
    public void like() {
        LikeRequest request = LikeRequest.builder()
                .restoId("id")
                .username("username")
                .build();

        when(commandExecutor.execute(LikeRestaurantCommandImpl.class, request))
                .thenReturn(Mono.just(new LikeResponse()));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/user/restaurant/like").build())
                .header("restaurant-id", "id")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(LikeRestaurantCommandImpl.class, request);
    }

    @Test
    public void beenThere() {
        BeenThereCommandRequest request = BeenThereCommandRequest.builder()
                .restoId("id")
                .username("username")
                .build();

        when(commandExecutor.execute(RestaurantBeenThereCommandImpl.class, request))
                .thenReturn(Mono.just(new LikeResponse()));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/user/restaurant/been-there").build())
                .header("restaurant-id", "id")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(RestaurantBeenThereCommandImpl.class, request);
    }

    @Test
    public void nearbyRestaurant() {
        NearbyRestaurantCommandRequest request = NearbyRestaurantCommandRequest.builder()
                .lat((double) 0)
                .lon((double) 0)
                .build();

        when(commandExecutor.execute(NearbyRestaurantCommandImpl.class, request))
                .thenReturn(Mono.just(Collections.singletonList(new RestaurantResponse())));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/restaurant/nearby")
                                .queryParam("lat", (double) 0)
                                .queryParam("lon", (double) 0)
                                .build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(NearbyRestaurantCommandImpl.class, request);
    }

    @Test
    public void searchRestaurant() {
        when(commandExecutor.execute(SearchRestaurantCommand.class, "query"))
                .thenReturn(Mono.just(Collections.singletonList(new RestaurantResponse())));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/restaurant/search")
                                .queryParam("q", "query")
                                .build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(SearchRestaurantCommand.class, "query");
    }

    @Test
    public void addPopularRestaurant() {
        PopularRestaurantRequest request = PopularRestaurantRequest.builder()
                .restoId("id")
                .expiredDay(DurationEnum.ONE_WEEK)
                .build();

        when(commandExecutor.execute(AddPopularRestaurantCommand.class, request))
                .thenReturn(Mono.just(new PopularRestaurantResponse()));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/merchant/restaurant/popular")
                                .build())
                .body(Mono.just(request), PopularRestaurantRequest.class)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(AddPopularRestaurantCommand.class, request);
    }

    @Test
    public void getPopularRestaurant() {
        when(commandExecutor.execute(GetPopularRestaurantCommand.class, ""))
                .thenReturn(Mono.just(Collections.singletonList(new RestaurantResponse())));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/restaurant/popular")
                                .build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(GetPopularRestaurantCommand.class, "");
    }

    @Test
    public void getAllRestaurant() {
        when(commandExecutor.execute(GetAllPendingRestaurantCommandImpl.class, ""))
                .thenReturn(Mono.just(Collections.singletonList(new RestaurantResponse())));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/admin/restaurant")
                                .build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(GetAllPendingRestaurantCommandImpl.class, "");
    }

    @Test
    public void approvePendingRestaurant() {
        ApprovePendingRestaurantCommandRequest request = ApprovePendingRestaurantCommandRequest.builder()
                .restaurantId("id")
                .build();

        when(commandExecutor.execute(ApprovePendingRestaurantCommandImpl.class, request))
                .thenReturn(Mono.just(new RestaurantResponse()));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/admin/restaurant/id")
                                .build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(ApprovePendingRestaurantCommandImpl.class, request);
    }

    @Test
    public void approveRequestRestaurantOwner() {
        ApproveRequestRestaurantOwnerCommandRequest request = ApproveRequestRestaurantOwnerCommandRequest.builder()
                .requestId("id")
                .build();

        when(commandExecutor.execute(ApproveRequestRestaurantOwnerCommand.class, request))
                .thenReturn(Mono.just(true));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/admin/restaurant/id/request")
                                .build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(ApproveRequestRestaurantOwnerCommand.class, request);
    }

    @Test
    public void getAvailableRestaurant() {
        GetAvailableRestaurantCommandRequest request = GetAvailableRestaurantCommandRequest.builder()
                .merchantUsername("username")
                .build();

        when(commandExecutor.execute(GetAvailableRestaurantCommand.class, request))
                .thenReturn(Mono.just(Collections.singletonList(new RestaurantResponse())));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/merchant/restaurant/available")
                                .build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(GetAvailableRestaurantCommand.class, request);
    }

    @Test
    public void requestRestaurant() {
        RequestRestaurantOwnerCommandRequest request = RequestRestaurantOwnerCommandRequest.builder()
                .merchantUsername("username")
                .restaurantId("id")
                .build();

        when(commandExecutor.execute(RequestRestaurantOwnerCommand.class, request))
                .thenReturn(Mono.just(new RestaurantResponse()));

        webTestClient.post()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/merchant/restaurant/id/request")
                                .build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(RequestRestaurantOwnerCommand.class, request);
    }

    @Test
    public void declineRestaurant() {
        when(commandExecutor.execute(DeclinePendingRestaurantRequest.class, "id"))
                .thenReturn(Mono.just(true));

        webTestClient.delete()
                .uri(uriBuilder ->
                        uriBuilder.path("/api/admin/restaurant/id")
                            .build())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessTokenEncrypted)
                .cookie("refreshToken", refreshTokenEncrypted)
                .exchange()
                .expectStatus()
                .isOk();

        verify(commandExecutor).execute(DeclinePendingRestaurantRequest.class, "id");
    }
}
