package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.GetAllRequestRestaurantOwnerCommandImpl;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.entity.RestaurantOwnerRequest;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.response.RequestRestaurantOwnerResponse;
import com.oofoodie.backend.repository.RestaurantOwnerRequestRepository;
import com.oofoodie.backend.repository.RestaurantRepository;
import com.oofoodie.backend.repository.UserRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAllRequestRestaurantOwnerCommandImplTest {

    @InjectMocks
    private GetAllRequestRestaurantOwnerCommandImpl command;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantOwnerRequestRepository restaurantOwnerRequestRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @After
    public void after() {
        verifyNoMoreInteractions(restaurantRepository, userRepository);
    }

    @Test
    public void executeTest() {
        RestaurantOwnerRequest restaurantOwnerRequest = RestaurantOwnerRequest.builder()
                .merchantUsername("username")
                .restaurantId("id")
                .build();

        when(restaurantOwnerRequestRepository.findAll())
                .thenReturn(Flux.fromIterable(Collections.singletonList(restaurantOwnerRequest)));
        when(restaurantRepository.findById("id")).thenReturn(Mono.just(new Restaurant()));
        when(userRepository.findByUsername("username")).thenReturn(Mono.just(new User()));

        StepVerifier.create(command.execute(""))
                .expectNext(Collections.singletonList(RequestRestaurantOwnerResponse.builder()
                        .restaurant(new Restaurant())
                        .merchant(new User())
                        .build()))
                .verifyComplete();

        verify(restaurantOwnerRequestRepository).findAll();
        verify(restaurantRepository).findById("id");
        verify(userRepository).findByUsername("username");
    }
}
