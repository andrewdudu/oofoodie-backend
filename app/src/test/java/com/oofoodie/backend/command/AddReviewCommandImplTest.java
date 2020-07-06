package com.oofoodie.backend.command;

import com.oofoodie.backend.BaseTest;
import com.oofoodie.backend.command.impl.AddReviewCommandImpl;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.ReviewRequest;
import com.oofoodie.backend.models.response.ReviewResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import com.oofoodie.backend.repository.ReviewRepository;
import com.oofoodie.backend.repository.TimelineRepository;
import com.oofoodie.backend.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class AddReviewCommandImplTest extends BaseTest {

    @InjectMocks
    private AddReviewCommandImpl command;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TimelineRepository timelineRepository;

    @Mock
    private GetRedisData getRedisData;

    @Before
    public void setUp() {
        initMocks(this);
    }

    private ReviewRequest constructReviewRequest() {
        return ReviewRequest.builder()
                .restoId(anyString())
                .user("user")
                .star(5)
                .build();
    }

    private Restaurant constructReviewRestaurant() {
        Restaurant restaurant = constructRestaurant();
        restaurant.setReviews(new ArrayList(Collections.singleton(constructReview())));

        return restaurant;
    }

    private ReviewResponse constructReviewResponse() {
        return ReviewResponse.builder()
                .star(5)
                .build();
    }

    private void mockRepository() {
        when(restaurantRepository.findById(anyString())).thenReturn(Mono.just(constructReviewRestaurant()));
        when(restaurantRepository.save(anyObject())).thenReturn(Mono.just(constructReviewRestaurant()));
        when(reviewRepository.save(constructReview())).thenReturn(Mono.just(constructReview()));
        when(userRepository.save(constructUser())).thenReturn(Mono.just(constructUser()));
        when(timelineRepository.save(anyObject())).thenReturn(Mono.just(constructTimeline()));
        when(getRedisData.getUser(anyString())).thenReturn(Mono.just(constructUser()));
    }

    @Test
    public void executeTest() {
        mockRepository();
        StepVerifier.create(command.execute(constructReviewRequest()))
                .expectNext(constructReviewResponse())
                .verifyComplete();
        verify(restaurantRepository).findById(anyString());
        verify(reviewRepository).save(constructReview());
        verify(userRepository).save(constructUser());
        verify(timelineRepository).save(constructTimeline());
        verify(getRedisData).getUser(anyString());
    }
}