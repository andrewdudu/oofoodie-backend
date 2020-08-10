package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.NearbyRestaurantCommandImpl;
import com.oofoodie.backend.models.elastic.RestaurantLocation;
import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.request.command.NearbyRestaurantCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;
import com.oofoodie.backend.repository.RestaurantLocationRepository;
import com.oofoodie.backend.repository.RestaurantRepository;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NearbyRestaurantCommandImplTest {

    @InjectMocks
    private NearbyRestaurantCommandImpl command;

    @Mock
    private RestaurantLocationRepository restaurantLocationRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @After
    public void after() {
        verifyNoMoreInteractions(restaurantLocationRepository, restaurantRepository);
    }

    @Test
    public void executeTest() {
        QueryBuilder query = QueryBuilders
                .geoDistanceQuery("location")
                .point(0, 0)
                .distance(10, DistanceUnit.KILOMETERS);
        RestaurantLocation restaurantLocation = new RestaurantLocation();
        restaurantLocation.setRestoId("id");

        when(restaurantLocationRepository.search(query)).thenReturn(Collections.singletonList(restaurantLocation));
        when(restaurantRepository.findByIdAndStatus("id", true)).thenReturn(Mono.just(new Restaurant()));

        StepVerifier.create(command.execute(NearbyRestaurantCommandRequest.builder()
                    .lat((double) 0)
                    .lon((double) 0)
                    .build()))
                .expectNext(Collections.singletonList(new RestaurantResponse()))
                .verifyComplete();

        verify(restaurantLocationRepository).search(query);
        verify(restaurantRepository).findByIdAndStatus("id", true);
    }
}
