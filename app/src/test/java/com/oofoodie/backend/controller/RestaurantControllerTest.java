package com.oofoodie.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oofoodie.backend.command.impl.AddRestaurantCommandImpl;
import com.oofoodie.backend.models.entity.Location;
import com.oofoodie.backend.models.request.Hour;
import com.oofoodie.backend.models.request.OpenHourRequest;
import com.oofoodie.backend.models.request.RestaurantRequest;
import com.oofoodie.backend.models.request.command.AddRestaurantCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerTest extends ControllerTest {

    @Captor
    private ArgumentCaptor<AddRestaurantCommandRequest> addRestaurantCommandRequestArgumentCaptor;

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

        givenAuth(restaurantRequest)
                .when()
                .post("/api/user/restaurant")
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(commandExecutor).execute(eq(AddRestaurantCommandImpl.class), addRestaurantCommandRequestArgumentCaptor.capture());

//        assertEquals(addRestaurantCommandRequestArgumentCaptor.getValue(), )
    }
}
