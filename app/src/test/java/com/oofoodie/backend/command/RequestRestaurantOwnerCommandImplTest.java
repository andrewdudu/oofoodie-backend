package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.RequestRestaurantOwnerCommandImpl;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.entity.RestaurantOwnerRequest;
import com.oofoodie.backend.models.request.command.RequestRestaurantOwnerCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;
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
public class RequestRestaurantOwnerCommandImplTest {

    @InjectMocks
    private RequestRestaurantOwnerCommandImpl command;

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
        Restaurant restaurant = Restaurant.builder()
                .merchantUsername("username")
                .build();
        restaurant.setId("id");
        RestaurantResponse restaurantResponse = RestaurantResponse.builder()
                .id("id")
                .build();

        when(restaurantRepository.findByMerchantUsername("username")).thenReturn(Mono.just(restaurant));
        when(restaurantOwnerRequestRepository.deleteByMerchantUsername("username")).thenReturn(Mono.empty());
        when(restaurantRepository.findById("id")).thenReturn(Mono.just(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(Mono.just(restaurant));

        StepVerifier.create(command.execute(RequestRestaurantOwnerCommandRequest.builder()
                    .restaurantId("id")
                    .merchantUsername("username")
                    .build()))
                .expectNext(restaurantResponse)
                .verifyComplete();

        verify(restaurantRepository).findByMerchantUsername("username");
        verify(restaurantOwnerRequestRepository).deleteByMerchantUsername("username");
        verify(restaurantRepository).findById("id");
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    public void executeTestNotFound() {
        Restaurant restaurant = Restaurant.builder()
                .merchantUsername("username")
                .build();
        restaurant.setId("id");
        RestaurantResponse restaurantResponse = RestaurantResponse.builder()
                .id("id")
                .merchantUsername("username")
                .build();

        when(restaurantRepository.findByMerchantUsername("username")).thenReturn(Mono.empty());
        when(restaurantRepository.findById("id")).thenReturn(Mono.just(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(Mono.just(restaurant));
        when(restaurantOwnerRequestRepository.save(any(RestaurantOwnerRequest.class))).thenReturn(Mono.empty());

        StepVerifier.create(command.execute(RequestRestaurantOwnerCommandRequest.builder()
                .restaurantId("id")
                .merchantUsername("username")
                .build()))
                .expectNext(restaurantResponse)
                .verifyComplete();

        verify(restaurantRepository).findByMerchantUsername("username");
        verify(restaurantRepository).findById("id");
        verify(restaurantRepository).save(restaurant);
        verify(restaurantOwnerRequestRepository).save(any(RestaurantOwnerRequest.class));
    }
}
