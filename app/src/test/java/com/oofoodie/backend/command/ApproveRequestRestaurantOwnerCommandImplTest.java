package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.ApproveRequestRestaurantOwnerCommandImpl;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.RestaurantOwnerRequest;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.command.ApproveRequestRestaurantOwnerCommandRequest;
import com.oofoodie.backend.repository.RestaurantOwnerRequestRepository;
import com.oofoodie.backend.repository.UserRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApproveRequestRestaurantOwnerCommandImplTest {

    @InjectMocks
    private ApproveRequestRestaurantOwnerCommandImpl command;

    @Mock
    private RestaurantOwnerRequestRepository restaurantOwnerRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GetRedisData getRedisData;

    @After
    public void after() {
        verifyNoMoreInteractions(userRepository, restaurantOwnerRequestRepository);
    }

    @Test
    public void executeTest() {
        RestaurantOwnerRequest restaurantOwnerRequest = RestaurantOwnerRequest.builder()
                .merchantUsername("username")
                .build();
        restaurantOwnerRequest.setId("id");

        when(restaurantOwnerRequestRepository.findById("id")).thenReturn(Mono.just(restaurantOwnerRequest));
        when(restaurantOwnerRequestRepository.deleteById("id")).thenReturn(Mono.empty());
        when(userRepository.findByUsername("username")).thenReturn(Mono.just(new User()));
        doNothing().when(getRedisData).saveUser(new User());
        when(userRepository.save(new User())).thenReturn(Mono.just(new User()));

        StepVerifier.create(command.execute(ApproveRequestRestaurantOwnerCommandRequest.builder()
                    .requestId("id")
                    .build()))
                .expectNext(true)
                .verifyComplete();

        verify(restaurantOwnerRequestRepository).findById("id");
        verify(restaurantOwnerRequestRepository).deleteById("id");
        verify(getRedisData).saveUser(new User());
        verify(userRepository).findByUsername("username");
    }
}
