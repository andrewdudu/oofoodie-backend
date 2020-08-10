package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.ApprovePendingRestaurantCommandImpl;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.ApprovePendingRestaurantCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApprovePendingRestaurantCommandImplTest {

    @InjectMocks
    private ApprovePendingRestaurantCommandImpl command;

    @Mock
    private RestaurantRepository restaurantRepository;

    @After
    public void after() {
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    public void executeTest() {
        Restaurant restaurant = new Restaurant();
        restaurant.setStatus(true);

        when(restaurantRepository.findById("id")).thenReturn(Mono.just(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(Mono.just(restaurant));

        StepVerifier.create(command.execute(ApprovePendingRestaurantCommandRequest.builder()
                    .restaurantId("id")
                    .build()))
                .expectNext(new RestaurantResponse())
                .verifyComplete();

        verify(restaurantRepository).findById("id");
        verify(restaurantRepository).save(restaurant);
    }
}
