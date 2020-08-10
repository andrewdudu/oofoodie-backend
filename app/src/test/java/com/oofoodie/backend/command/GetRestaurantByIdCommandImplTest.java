package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.GetRestaurantByIdCommandImpl;
import com.oofoodie.backend.exception.NotFoundException;
import com.oofoodie.backend.helper.CalculateRatingStatsHelper;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.entity.Review;
import com.oofoodie.backend.models.response.RatingStats;
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

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class GetRestaurantByIdCommandImplTest {

    @InjectMocks
    private GetRestaurantByIdCommandImpl command;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private CalculateRatingStatsHelper calculateRatingStatsHelper;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    private void mockRepositoryCall(Mono<Restaurant> repositoryResponse) {
        when(restaurantRepository.findByIdAndStatus("id", true))
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
        StepVerifier.create(command.execute("id"))
                .expectNext(constructRestaurantResponseObject())
                .verifyComplete();
        verify(restaurantRepository).findByIdAndStatus("id", true);
    }

    @Test
    public void executeTestRatingStatusNotNull() {
        Restaurant restaurant = constructRestaurantObject();
        restaurant.setReviews(Arrays.asList(new Review()));
        when(calculateRatingStatsHelper.calculateRatingStats(restaurant))
                .thenReturn(new RatingStats());
        when(restaurantRepository.findByIdAndStatus("id", true))
                .thenReturn(Mono.just(restaurant));

        StepVerifier.create(command.execute("id"))
                .expectNext(RestaurantResponse.builder()
                        .address("street")
                        .ratingStats(new RatingStats())
                        .reviews(Arrays.asList(new Review()))
                        .build())
                .verifyComplete();

        verify(restaurantRepository).findByIdAndStatus("id", true);
        verify(calculateRatingStatsHelper).calculateRatingStats(restaurant);
    }

    @Test
    public void executeNotFoundTest() {
        mockRepositoryCall(Mono.empty());
        StepVerifier.create(command.execute("id"))
                .expectError(NotFoundException.class)
                .verify();
        verify(restaurantRepository).findByIdAndStatus("id", true);
    }
}
