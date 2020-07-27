package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.TopupCreditCommandImpl;
import com.oofoodie.backend.models.entity.CreditOrder;
import com.oofoodie.backend.models.request.command.TopupCreditCommandRequest;
import com.oofoodie.backend.models.response.TopupCreditResponse;
import com.oofoodie.backend.repository.CreditOrderRepository;
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

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopupCreditCommandImplTest {

    @InjectMocks
    private TopupCreditCommandImpl command;

    @Mock
    private CreditOrderRepository creditOrderRepository;

    @Captor
    private ArgumentCaptor<CreditOrder> creditOrderCaptor;

    @After
    public void after() {
        verifyNoMoreInteractions(creditOrderRepository);
    }

    @Test
    public void executeTest() {
        when(creditOrderRepository.save(any(CreditOrder.class))).thenReturn(Mono.just(new CreditOrder()));

        StepVerifier.create(command.execute(new TopupCreditCommandRequest()))
                .expectNext(new TopupCreditResponse())
                .verifyComplete();

        verify(creditOrderRepository).save(creditOrderCaptor.capture());

        assertNotNull(creditOrderCaptor.getValue().getId());
    }
}
