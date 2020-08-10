package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.DeclineRequestRestaurantOwnerCommandImpl;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.entity.RestaurantOwnerRequest;
import com.oofoodie.backend.repository.RestaurantOwnerRequestRepository;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeclineRequestRestaurantOwnerCommandImplTest {

    @InjectMocks
    private DeclineRequestRestaurantOwnerCommandImpl command;

    @Mock
    private RestaurantOwnerRequestRepository restaurantOwnerRequestRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(restaurantOwnerRequestRepository, restaurantRepository);
    }

    @Test
    public void executeTest() {
        RestaurantOwnerRequest restaurantOwnerRequest = new RestaurantOwnerRequest();
        restaurantOwnerRequest.setRestaurantId("id");

        when(restaurantOwnerRequestRepository.findById("id")).thenReturn(Mono.just(restaurantOwnerRequest));
        when(restaurantOwnerRequestRepository.deleteById("id")).thenReturn(Mono.empty());
        when(restaurantRepository.findById("id")).thenReturn(Mono.just(new Restaurant()));
        when(restaurantRepository.save(new Restaurant())).thenReturn(Mono.just(new Restaurant()));

        StepVerifier.create(command.execute("id"))
                .expectNext(true)
                .verifyComplete();

        verify(restaurantOwnerRequestRepository).findById("id");
        verify(restaurantOwnerRequestRepository).deleteById("id");
        verify(restaurantRepository).findById("id");
        verify(restaurantRepository).save(new Restaurant());
    }
}
