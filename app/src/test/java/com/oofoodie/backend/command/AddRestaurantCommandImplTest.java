package com.oofoodie.backend.command;

import com.blibli.oss.command.CommandExecutor;
import com.oofoodie.backend.BaseTest;
import com.oofoodie.backend.command.impl.AddImageCommandImpl;
import com.oofoodie.backend.command.impl.AddRestaurantCommandImpl;
import com.oofoodie.backend.models.entity.Location;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.AddRestaurantCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantLocationRepository;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.ElasticsearchException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class AddRestaurantCommandImplTest extends BaseTest {

    @InjectMocks
    private AddRestaurantCommandImpl command;

    @Mock
    private RestaurantLocationRepository restaurantLocationRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private CommandExecutor commandExecutor;

    @Before
    public void setUp() {
        initMocks(this);
    }

    private void mockRepository() {
        constructRestaurantLocation();
        when(restaurantLocationRepository.save(anyObject())).thenReturn(constructRestaurantLocation());
        when(restaurantRepository.save(constructRestaurant())).thenReturn(Mono.just(constructRestaurant()));
        when(commandExecutor.execute(AddImageCommandImpl.class, "success")).thenReturn(Mono.just("success"));
    }

    private AddRestaurantCommandRequest constructAddRestaurantCommandRequest() {
        return AddRestaurantCommandRequest.builder()
                .location(constructLocation())
                .image("success")
                .openHour(constructOpenHourRequest())
                .build();
    }

    private RestaurantResponse constructRestaurantResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        BeanUtils.copyProperties(restaurant, response);

        response.setImage("success");

        return response;
    }

    @Test
    public void executeTest() {;
        mockRepository();
        StepVerifier.create(command.execute(constructAddRestaurantCommandRequest()))
                .expectNext(constructRestaurantResponse(constructRestaurant()))
                .verifyComplete();
        verify(restaurantLocationRepository).save(anyObject());
        verify(restaurantRepository).save(constructRestaurant());
    }

    @Test
    public void executeTestNoLat() {;
        StepVerifier.create(command.execute(
                AddRestaurantCommandRequest.builder()
                    .location(Location.builder()
                            .lon((float) 3.0)
                            .lat(null)
                            .build())
                    .image("success")
                    .openHour(constructOpenHourRequest())
                    .build()))
                .expectError(ElasticsearchException.class)
                .verify();
    }

    @Test
    public void executeTestNoLon() {;
        StepVerifier.create(command.execute(
                AddRestaurantCommandRequest.builder()
                        .location(Location.builder()
                                .lat((float) 3.0)
                                .lon(null)
                                .build())
                        .image("success")
                        .openHour(constructOpenHourRequest())
                        .build()))
                .expectError(ElasticsearchException.class)
                .verify();
    }

    @Test
    public void executeTestNoLocation() {;
        StepVerifier.create(command.execute(
                AddRestaurantCommandRequest.builder()
                        .location(null)
                        .image("success")
                        .openHour(constructOpenHourRequest())
                        .build()))
                .expectError(ElasticsearchException.class)
                .verify();
    }
}
