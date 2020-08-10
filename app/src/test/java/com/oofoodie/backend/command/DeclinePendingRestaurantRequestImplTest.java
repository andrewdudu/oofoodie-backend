package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.DeclinePendingRestaurantRequestImpl;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.models.entity.Restaurant;
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
public class DeclinePendingRestaurantRequestImplTest {

    @InjectMocks
    private DeclinePendingRestaurantRequestImpl command;

    @Mock
    private RestaurantRepository restaurantRepository;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    public void executeTest() {
        when(restaurantRepository.findByIdAndStatus("id", false)).thenReturn(Mono.just(new Restaurant()));
        when(restaurantRepository.deleteById("id")).thenReturn(Mono.empty());

        StepVerifier.create(command.execute("id"))
                .verifyComplete();

        verify(restaurantRepository).findByIdAndStatus("id", false);
        verify(restaurantRepository).deleteById("id");
    }

    @Test
    public void executeTestBadRequest() {
        when(restaurantRepository.findByIdAndStatus("id", false)).thenReturn(Mono.empty());

        StepVerifier.create(command.execute("id"))
                .expectError(BadRequestException.class)
                .verify();

        verify(restaurantRepository).findByIdAndStatus("id", false);
    }
}
