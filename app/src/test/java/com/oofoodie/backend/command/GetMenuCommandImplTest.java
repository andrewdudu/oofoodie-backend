package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.GetMenuCommandImpl;
import com.oofoodie.backend.models.entity.Menu;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.GetMenuCommandRequest;
import com.oofoodie.backend.models.response.MenuResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetMenuCommandImplTest {

    @InjectMocks
    private GetMenuCommandImpl command;

    @Mock
    private RestaurantRepository restaurantRepository;

    @After
    public void after() {
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    public void executeTestNoMenus() {
        when(restaurantRepository.findById("id")).thenReturn(Mono.just(new Restaurant()));

        StepVerifier.create(command.execute(GetMenuCommandRequest.builder()
                    .restaurantId("id")
                    .build()))
                .expectNext(new ArrayList<>())
                .verifyComplete();

        verify(restaurantRepository).findById("id");
    }

    @Test
    public void executeTest() {
        Restaurant restaurant = Restaurant.builder()
                .menus(Collections.singletonList(new Menu()))
                .build();

        when(restaurantRepository.findById("id")).thenReturn(Mono.just(restaurant));

        StepVerifier.create(command.execute(GetMenuCommandRequest.builder()
                    .restaurantId("id")
                    .build()))
                .expectNext(Collections.singletonList(new MenuResponse()))
                .verifyComplete();

        verify(restaurantRepository).findById("id");
    }
}
