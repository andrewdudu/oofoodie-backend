package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.OrderMenuCommandImpl;
import com.oofoodie.backend.models.entity.Orders;
import com.oofoodie.backend.models.entity.StatusEnum;
import com.oofoodie.backend.models.request.command.OrderMenuCommandRequest;
import com.oofoodie.backend.repository.OrderRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderMenuCommandImplTest {

    @InjectMocks
    private OrderMenuCommandImpl command;

    @Mock
    private OrderRepository orderRepository;

    @Captor
    private ArgumentCaptor<Orders> ordersCaptor;

    @After
    public void after() {
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void executeTest() {
        when(orderRepository.save(any(Orders.class))).thenReturn(Mono.just(new Orders()));

        StepVerifier.create(command.execute(new OrderMenuCommandRequest()))
                .expectNext(true)
                .verifyComplete();

        verify(orderRepository).save(ordersCaptor.capture());

        assertEquals(StatusEnum.ON_GOING.toString(), ordersCaptor.getValue().getStatus());
    }
}
