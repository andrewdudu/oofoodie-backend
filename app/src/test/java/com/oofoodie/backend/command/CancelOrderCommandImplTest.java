package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.CancelOrderCommandImpl;
import com.oofoodie.backend.models.entity.Orders;
import com.oofoodie.backend.models.entity.StatusEnum;
import com.oofoodie.backend.models.request.command.CancelOrderCommandRequest;
import com.oofoodie.backend.repository.OrderRepository;
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
public class CancelOrderCommandImplTest {

    @InjectMocks
    private CancelOrderCommandImpl command;

    @Mock
    private OrderRepository orderRepository;

    @After
    public void after() {
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    public void executeTest() {
        Orders orders = Orders.builder().status(StatusEnum.CANCELLED.toString()).build();
        when(orderRepository.findById("id")).thenReturn(Mono.just(new Orders()));
        when(orderRepository.save(orders)).thenReturn(Mono.just(orders));

        StepVerifier.create(command.execute(new CancelOrderCommandRequest("username", "id")))
                .expectNext(true)
                .verifyComplete();

        verify(orderRepository).findById("id");
        verify(orderRepository).save(orders);
    }
}
