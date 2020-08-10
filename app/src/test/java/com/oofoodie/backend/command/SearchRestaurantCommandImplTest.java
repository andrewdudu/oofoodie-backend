package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.SearchRestaurantCommandImpl;
import com.oofoodie.backend.helper.CalculateRatingStatsHelper;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.entity.Review;
import com.oofoodie.backend.models.response.RatingStats;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantCustomRepository;
import org.junit.jupiter.api.AfterEach;
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
public class SearchRestaurantCommandImplTest {

    @InjectMocks
    private SearchRestaurantCommandImpl command;

    @Mock
    private RestaurantCustomRepository restaurantCustomRepository;

    @Mock
    private CalculateRatingStatsHelper calculateRatingStatsHelper;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(calculateRatingStatsHelper, restaurantCustomRepository);
    }

    @Test
    public void executeTest() {
        when(restaurantCustomRepository.search("magic"))
                .thenReturn(Flux.fromIterable(Collections.singletonList(new Restaurant())));

        StepVerifier.create(command.execute("magic"))
                .expectNext(Collections.singletonList(RestaurantResponse.builder().build()))
                .verifyComplete();

        verify(restaurantCustomRepository).search("magic");
    }

    @Test
    public void executeTestHasReview() {
        Restaurant restaurant = Restaurant.builder()
                .reviews(Collections.singletonList(new Review()))
                .build();

        when(restaurantCustomRepository.search("magic"))
                .thenReturn(Flux.fromIterable(Collections.singletonList(restaurant)));
        when(calculateRatingStatsHelper.calculateRatingStats(restaurant)).thenReturn(new RatingStats());

        StepVerifier.create(command.execute("magic"))
                .expectNext(Collections.singletonList(RestaurantResponse.builder()
                        .reviews(Collections.singletonList(new Review()))
                        .ratingStats(new RatingStats())
                        .build()))
                .verifyComplete();

        verify(restaurantCustomRepository).search("magic");
        verify(calculateRatingStatsHelper).calculateRatingStats(restaurant);
    }
}
