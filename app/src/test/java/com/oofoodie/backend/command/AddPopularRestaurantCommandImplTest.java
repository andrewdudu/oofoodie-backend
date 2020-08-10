package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.AddPopularRestaurantCommandImpl;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.models.entity.DurationEnum;
import com.oofoodie.backend.models.entity.PopularRestaurant;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.PopularRestaurantRequest;
import com.oofoodie.backend.models.response.PopularRestaurantResponse;
import com.oofoodie.backend.repository.PopularRestaurantRepository;
import com.oofoodie.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddPopularRestaurantCommandImplTest {

    @InjectMocks
    private AddPopularRestaurantCommandImpl command;

    @Mock
    private PopularRestaurantRepository popularRestaurantRepository;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<PopularRestaurant> popularRestaurantCaptor;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(userRepository, popularRestaurantRepository);
    }

    @Test
    public void executeTest() {
        User user = User.builder()
                .credits(BigDecimal.valueOf(30000))
                .build();
        User userZero = User.builder()
                .credits(BigDecimal.ZERO)
                .build();

        when(userRepository.findByRestaurantOwner("id")).thenReturn(Mono.just(user));
        when(userRepository.save(userZero)).thenReturn(Mono.just(new User()));
        when(popularRestaurantRepository.save(any(PopularRestaurant.class))).thenReturn(Mono.just(new PopularRestaurant()));

        StepVerifier.create(command.execute(PopularRestaurantRequest.builder()
                    .expiredDay(DurationEnum.ONE_WEEK)
                    .restoId("id")
                    .build()))
                .expectNext(new PopularRestaurantResponse())
                .verifyComplete();

        verify(userRepository).findByRestaurantOwner("id");
        verify(userRepository).save(userZero);
        verify(popularRestaurantRepository).save(popularRestaurantCaptor.capture());

        assertNotNull(popularRestaurantCaptor.getValue().getExpiredDateInSeconds());
    }

    @Test
    public void executeTestBadRequest() {
        User user = User.builder()
                .credits(BigDecimal.valueOf(10000))
                .build();

        when(userRepository.findByRestaurantOwner("id")).thenReturn(Mono.just(user));
        StepVerifier.create(command.execute(PopularRestaurantRequest.builder()
                    .expiredDay(DurationEnum.ONE_WEEK)
                    .restoId("id")
                    .build()))
                .expectError(BadRequestException.class)
                .verify();

        verify(userRepository).findByRestaurantOwner("id");
    }
}
