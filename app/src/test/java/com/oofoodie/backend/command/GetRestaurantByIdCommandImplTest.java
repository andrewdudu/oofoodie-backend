package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.GetRestaurantByIdCommandImpl;
import com.oofoodie.backend.exception.NotFoundException;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class GetRestaurantByIdCommandImplTest {

    @InjectMocks
    private GetRestaurantByIdCommandImpl command;

    @Mock
    private RestaurantRepository restaurantRepository;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    private void mockRepositoryCall(Mono<Restaurant> repositoryResponse) {
        when(restaurantRepository.findById(anyString()))
                .thenReturn(repositoryResponse);
    }

    private Restaurant constructRestaurantObject() {
        return Restaurant.builder().address("street").build();
    }

    private RestaurantResponse constructRestaurantResponseObject() {
        return RestaurantResponse.builder().address("street").build();
    }

    @Test
    public void executeTest() {
        mockRepositoryCall(Mono.just(constructRestaurantObject()));
        StepVerifier.create(command.execute(anyString()))
                .expectNext(constructRestaurantResponseObject())
                .verifyComplete();
        verify(restaurantRepository).findById(anyString());
    }

    @Test
    public void executeNotFoundTest() {
        mockRepositoryCall(Mono.empty());
        StepVerifier.create(command.execute(anyString()))
                .expectError(NotFoundException.class)
                .verify();
        verify(restaurantRepository).findById(anyString());
    }
}
