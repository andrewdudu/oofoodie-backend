package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.LikeRestaurantCommandImpl;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.LikeRequest;
import com.oofoodie.backend.models.response.LikeResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import com.oofoodie.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeRestaurantCommandImplTest {

    @InjectMocks
    private LikeRestaurantCommandImpl command;

    @Mock
    private GetRedisData getRedisData;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(userRepository, restaurantRepository, getRedisData);
    }

    @Test
    public void executeTest() {
        Restaurant restaurant = Restaurant.builder()
                .likes(new ArrayList<>(Collections.singletonList("username")))
                .build();
        User user = User.builder()
                .likes(new ArrayList<>(Collections.singletonList("username")))
                .username("username")
                .build();
        User userLiked = User.builder()
                .likes(new ArrayList<>(Arrays.asList("username", "id")))
                .username("username")
                .build();

        when(restaurantRepository.findByIdAndStatus("id", true)).thenReturn(Mono.just(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(Mono.just(restaurant));
        when(userRepository.save(userLiked)).thenReturn(Mono.just(userLiked));
        when(getRedisData.getUser("username")).thenReturn(Mono.just(user));
        doNothing().when(getRedisData).saveUser(userLiked);

        StepVerifier.create(command.execute(LikeRequest.builder()
                    .restoId("id")
                    .username("username")
                    .build()))
                .expectNext(new LikeResponse("success"))
                .verifyComplete();

        verify(restaurantRepository).findByIdAndStatus("id", true);
        verify(restaurantRepository).save(restaurant);
        verify(getRedisData).getUser("username");
        verify(userRepository).save(userLiked);
        verify(getRedisData).saveUser(userLiked);
    }

    @Test
    public void executeTestButNoLikes() {
        Restaurant restaurant = Restaurant.builder()
                .likes(new ArrayList<>(Collections.singletonList("user")))
                .build();
        User user = User.builder()
                .likes(new ArrayList<>(Collections.singletonList("id")))
                .username("username")
                .build();
        User userLiked = User.builder()
                .likes(new ArrayList<>())
                .username("username")
                .build();

        when(restaurantRepository.findByIdAndStatus("id", true)).thenReturn(Mono.just(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(Mono.just(restaurant));
        when(userRepository.save(userLiked)).thenReturn(Mono.just(userLiked));
        when(getRedisData.getUser("username")).thenReturn(Mono.just(user));
        doNothing().when(getRedisData).saveUser(userLiked);

        StepVerifier.create(command.execute(LikeRequest.builder()
                .restoId("id")
                .username("username")
                .build()))
                .expectNext(new LikeResponse("success"))
                .verifyComplete();

        verify(restaurantRepository).findByIdAndStatus("id", true);
        verify(restaurantRepository).save(restaurant);
        verify(getRedisData).getUser("username");
        verify(userRepository).save(userLiked);
        verify(getRedisData).saveUser(userLiked);
    }

    @Test
    public void executeTestLikesIsNull() {
        Restaurant restaurant = Restaurant.builder()
                .build();
        User user = User.builder()
                .username("username")
                .build();
        User userLiked = User.builder()
                .likes(new ArrayList<>(Collections.singletonList("id")))
                .username("username")
                .build();

        when(restaurantRepository.findByIdAndStatus("id", true)).thenReturn(Mono.just(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(Mono.just(restaurant));
        when(userRepository.save(userLiked)).thenReturn(Mono.just(userLiked));
        when(getRedisData.getUser("username")).thenReturn(Mono.just(user));
        doNothing().when(getRedisData).saveUser(userLiked);

        StepVerifier.create(command.execute(LikeRequest.builder()
                .restoId("id")
                .username("username")
                .build()))
                .expectNext(new LikeResponse("success"))
                .verifyComplete();

        verify(restaurantRepository).findByIdAndStatus("id", true);
        verify(restaurantRepository).save(restaurant);
        verify(getRedisData).getUser("username");
        verify(userRepository).save(userLiked);
        verify(getRedisData).saveUser(userLiked);
    }
}
