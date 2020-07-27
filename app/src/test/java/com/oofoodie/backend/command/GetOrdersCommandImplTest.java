package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.GetOrdersCommandImpl;
import com.oofoodie.backend.helper.CalculateRatingStatsHelper;
import com.oofoodie.backend.models.entity.Orders;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.GetOrdersCommandRequest;
import com.oofoodie.backend.models.response.OrdersResponse;
import com.oofoodie.backend.models.response.RatingStats;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.OrderRepository;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetOrdersCommandImplTest {

    @InjectMocks
    private GetOrdersCommandImpl command;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private CalculateRatingStatsHelper calculateRatingStatsHelper;

    @Mock
    private OrderRepository orderRepository;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(orderRepository, calculateRatingStatsHelper, restaurantRepository);
    }

    @Test
    public void executeTestNoRating() {
        when(orderRepository.findAllByUsername("username"))
                .thenReturn(Flux.fromIterable(Collections.singletonList(Orders.builder().restaurantId("id").build())));
        when(restaurantRepository.findById("id")).thenReturn(Mono.just(new Restaurant()));

        StepVerifier.create(command.execute(GetOrdersCommandRequest.builder()
                    .username("username")
                    .build()))
                .expectNext(Collections.singletonList(OrdersResponse.builder()
                        .restaurantResponse(new RestaurantResponse())
                        .build()))
                .verifyComplete();

        verify(orderRepository).findAllByUsername("username");
        verify(restaurantRepository).findById("id");
    }

    @Test
    public void executeTest() {
        Restaurant restaurant = Restaurant.builder()
                .reviews(new ArrayList<>())
                .build();

        when(orderRepository.findAllByUsername("username"))
                .thenReturn(Flux.fromIterable(Collections.singletonList(Orders.builder().restaurantId("id").build())));
        when(restaurantRepository.findById("id"))
                .thenReturn(Mono.just(restaurant));
        when(calculateRatingStatsHelper.calculateRatingStats(restaurant)).thenReturn(new RatingStats());

        StepVerifier.create(command.execute(GetOrdersCommandRequest.builder()
                .username("username")
                .build()))
                .expectNext(Collections.singletonList(OrdersResponse.builder()
                        .restaurantResponse(RestaurantResponse.builder()
                                .ratingStats(new RatingStats())
                                .reviews(new ArrayList<>())
                                .build())
                        .build()))
                .verifyComplete();

        verify(orderRepository).findAllByUsername("username");
        verify(restaurantRepository).findById("id");
    }
}
