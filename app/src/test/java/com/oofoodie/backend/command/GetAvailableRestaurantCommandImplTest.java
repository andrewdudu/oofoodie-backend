package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.GetAvailableRestaurantCommandImpl;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.GetAvailableRestaurantCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAvailableRestaurantCommandImplTest {

    @InjectMocks
    private GetAvailableRestaurantCommandImpl command;

    @Mock
    private RestaurantRepository restaurantRepository;

    @After
    public void verifyNoInteractions() {
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    public void executeTest() {
        when(restaurantRepository.findAllByMerchantUsernameOrMerchantUsername("username", null))
                .thenReturn(Flux.fromIterable(Collections.singletonList(new Restaurant())));

        StepVerifier.create(command.execute(GetAvailableRestaurantCommandRequest.builder()
                    .merchantUsername("username")
                    .build()))
                .expectNext(Collections.singletonList(new RestaurantResponse()))
                .verifyComplete();

        Mockito.verify(restaurantRepository).findAllByMerchantUsernameOrMerchantUsername("username", null);
    }
}
