package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.GetPopularRestaurantCommandImpl;
import com.oofoodie.backend.helper.WebResponseConstructor;
import com.oofoodie.backend.models.entity.PopularRestaurant;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.PopularRestaurantRepository;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetPopularRestaurantImplTest {

    @InjectMocks
    private GetPopularRestaurantCommandImpl command;

    @Mock
    private PopularRestaurantRepository popularRestaurantRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private WebResponseConstructor webResponseConstructor;

    @Test
    public void executeTest() {
        List<PopularRestaurant> popularRestaurants = Collections.singletonList(PopularRestaurant.builder().restoId("id").build());
        when(popularRestaurantRepository.findAll()).thenReturn(Flux.fromIterable(popularRestaurants));
        when(restaurantRepository.findByIdAndStatus("id", true)).thenReturn(Mono.just(new Restaurant()));
        when(webResponseConstructor.toRestaurantResponse(new Restaurant())).thenReturn(new RestaurantResponse());

        StepVerifier.create(command.execute(""))
                .expectNext(Collections.singletonList(new RestaurantResponse()))
                .verifyComplete();

        verify(popularRestaurantRepository).findAll();
        verify(restaurantRepository).findByIdAndStatus("id", true);
        verify(webResponseConstructor).toRestaurantResponse(new Restaurant());
    }
}
