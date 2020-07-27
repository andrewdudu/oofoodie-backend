package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.EditMenuCommandImpl;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.models.entity.Menu;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.EditMenuCommandRequest;
import com.oofoodie.backend.models.response.MenuResponse;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.junit.jupiter.api.AfterEach;
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
public class EditMenuCommandImplTest {

    @InjectMocks
    private EditMenuCommandImpl command;

    @Mock
    private RestaurantRepository restaurantRepository;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(restaurantRepository);
    }

    @Test
    public void executeTest() {
        Restaurant restaurant = Restaurant.builder()
                .merchantUsername("username")
                .build();
        when(restaurantRepository.findById("id"))
                .thenReturn(Mono.just(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(Mono.just(restaurant));

        StepVerifier.create(command.execute(EditMenuCommandRequest.builder()
                    .merchantUsername("username")
                    .restaurantId("id")
                    .menu(new ArrayList<>(Collections.singletonList(new Menu())))
                    .build()))
                .expectNext(Collections.singletonList(new MenuResponse()))
                .verifyComplete();

        verify(restaurantRepository).findById("id");
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    public void executeTestBadRequest() {
        Restaurant restaurant = Restaurant.builder()
                .merchantUsername("username")
                .build();
        when(restaurantRepository.findById("id"))
                .thenReturn(Mono.just(restaurant));

        StepVerifier.create(command.execute(EditMenuCommandRequest.builder()
                    .merchantUsername("user")
                    .restaurantId("id")
                    .menu(new ArrayList<>(Collections.singletonList(new Menu())))
                    .build()))
                .expectError(BadRequestException.class)
                .verify();

        verify(restaurantRepository).findById("id");
    }
}
