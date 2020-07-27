package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.RestaurantBeenThereCommandImpl;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.BeenThereCommandRequest;
import com.oofoodie.backend.models.response.LikeResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantBeenThereCommandImplTest {

    @InjectMocks
    private RestaurantBeenThereCommandImpl command;

    @Mock
    private RestaurantRepository restaurantRepository;

    @AfterEach
    public void after() {
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    public void executeTest() {
        Restaurant restaurant = Restaurant.builder()
                .beenThere(Collections.singletonList("username"))
                .build();

        when(restaurantRepository.findByIdAndStatus("id", true)).thenReturn(Mono.just(new Restaurant()));
        when(restaurantRepository.save(restaurant)).thenReturn(Mono.just(restaurant));

        StepVerifier.create(command.execute(BeenThereCommandRequest.builder()
                    .restoId("id")
                    .username("username")
                    .build()))
                .expectNext(new LikeResponse("success"))
                .verifyComplete();

        verify(restaurantRepository).findByIdAndStatus("id", true);
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    public void executeTestAlreadyBeenThere() {
        Restaurant restaurant = Restaurant.builder()
                .beenThere(Collections.singletonList("username"))
                .build();

        when(restaurantRepository.findByIdAndStatus("id", true))
                .thenReturn(Mono.just(Restaurant.builder().beenThere(Collections.singletonList("username")).build()));
        when(restaurantRepository.save(restaurant)).thenReturn(Mono.just(restaurant));

        StepVerifier.create(command.execute(BeenThereCommandRequest.builder()
                .restoId("id")
                .username("username")
                .build()))
                .expectNext(new LikeResponse("success"))
                .verifyComplete();

        verify(restaurantRepository).findByIdAndStatus("id", true);
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    public void executeTestNotBeenThere() {
        Restaurant restaurant = Restaurant.builder()
                .beenThere(Arrays.asList("user", "username"))
                .build();

        when(restaurantRepository.findByIdAndStatus("id", true))
                .thenReturn(Mono.just(Restaurant.builder().beenThere(new ArrayList<>(Collections.singletonList("user"))).build()));
        when(restaurantRepository.save(restaurant)).thenReturn(Mono.just(restaurant));

        StepVerifier.create(command.execute(BeenThereCommandRequest.builder()
                .restoId("id")
                .username("username")
                .build()))
                .expectNext(new LikeResponse("success"))
                .verifyComplete();

        verify(restaurantRepository).findByIdAndStatus("id", true);
        verify(restaurantRepository).save(restaurant);
    }
}
