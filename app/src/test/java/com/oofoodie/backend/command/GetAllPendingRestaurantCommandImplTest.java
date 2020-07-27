package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.GetAllPendingRestaurantCommandImpl;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAllPendingRestaurantCommandImplTest {

    @InjectMocks
    private GetAllPendingRestaurantCommandImpl command;

    @Mock
    private RestaurantRepository restaurantRepository;

    @After
    public void after() {
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    public void executeTest() {
        when(restaurantRepository.findAllByStatus(false)).thenReturn(Flux.fromIterable(Collections.singletonList(new Restaurant())));

        StepVerifier.create(command.execute(""))
                .expectNext(Collections.singletonList(new RestaurantResponse()))
                .verifyComplete();

        verify(restaurantRepository).findAllByStatus(false);
    }

}
