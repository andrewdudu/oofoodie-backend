package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.GetIncomingOrdersCommandImpl;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.Orders;
import com.oofoodie.backend.models.entity.StatusEnum;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.response.OrdersResponse;
import com.oofoodie.backend.repository.OrderRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetIncomingOrdersCommandImplTest {

    @InjectMocks
    private GetIncomingOrdersCommandImpl command;

    @Mock
    private GetRedisData getRedisData;

    @Mock
    private OrderRepository orderRepository;

    @After
    public void after() {
        verifyNoMoreInteractions(orderRepository, getRedisData);
    }

    @Test
    public void executeTest() {
        OrdersResponse ordersResponse = OrdersResponse.builder()
                .status(StatusEnum.ON_GOING.toString())
                .build();

        when(getRedisData.getUser("username")).thenReturn(Mono.just(User.builder().restaurantOwner("id").build()));
        when(orderRepository.findAllByRestaurantIdAndStatus("id", StatusEnum.ON_GOING.toString()))
            .thenReturn(Flux.fromIterable(Collections.singletonList(new Orders())));

        StepVerifier.create(command.execute("username"))
                .expectNext(Collections.singletonList(ordersResponse))
                .verifyComplete();

        verify(getRedisData).getUser("username");
        verify(orderRepository).findAllByRestaurantIdAndStatus("id", StatusEnum.ON_GOING.toString());
    }
}
