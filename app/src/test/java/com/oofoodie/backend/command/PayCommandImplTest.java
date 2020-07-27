package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.PayCommandImpl;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.CreditOrder;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.PayRequest;
import com.oofoodie.backend.repository.CreditOrderRepository;
import com.oofoodie.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PayCommandImplTest {

    @InjectMocks
    private PayCommandImpl command;

    @Mock
    private CreditOrderRepository creditOrderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GetRedisData getRedisData;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(getRedisData, userRepository, creditOrderRepository);
    }

    @Test
    public void executeTest() {
        CreditOrder creditOrder = CreditOrder.builder()
                .credit(BigDecimal.ONE)
                .merchantUsername("username")
                .build();
        creditOrder.setId("id");

        when(creditOrderRepository.findById("id")).thenReturn(Mono.just(creditOrder));
        when(getRedisData.getUser("username")).thenReturn(Mono.just(new User()));
        when(userRepository.save(User.builder().credits(BigDecimal.ONE).build()))
                .thenReturn(Mono.just(new User()));
        doNothing().when(getRedisData).saveUser(User.builder().credits(BigDecimal.ONE).build());
        when(creditOrderRepository.deleteById("id")).thenReturn(Mono.empty());

        StepVerifier.create(command.execute(PayRequest.builder()
                    .creditOrderId("id")
                    .build()))
                .expectNext(true)
                .verifyComplete();

        verify(creditOrderRepository).findById("id");
        verify(getRedisData).getUser("username");
        verify(userRepository).save(User.builder().credits(BigDecimal.ONE).build());
        verify(getRedisData).saveUser(User.builder().credits(BigDecimal.ONE).build());
        verify(creditOrderRepository).deleteById("id");
    }

    @Test
    public void executeTestNoCredit() {
        CreditOrder creditOrder = CreditOrder.builder()
                .credit(BigDecimal.ONE)
                .merchantUsername("username")
                .build();
        creditOrder.setId("id");

        when(creditOrderRepository.findById("id")).thenReturn(Mono.just(creditOrder));
        when(getRedisData.getUser("username"))
                .thenReturn(Mono.just(User.builder().credits(BigDecimal.ONE).build()));
        when(userRepository.save(User.builder().credits(BigDecimal.valueOf(2)).build()))
                .thenReturn(Mono.just(new User()));
        doNothing().when(getRedisData).saveUser(User.builder().credits(BigDecimal.valueOf(2)).build());
        when(creditOrderRepository.deleteById("id")).thenReturn(Mono.empty());

        StepVerifier.create(command.execute(PayRequest.builder()
                .creditOrderId("id")
                .build()))
                .expectNext(true)
                .verifyComplete();

        verify(creditOrderRepository).findById("id");
        verify(getRedisData).getUser("username");
        verify(userRepository).save(User.builder().credits(BigDecimal.valueOf(2)).build());
        verify(getRedisData).saveUser(User.builder().credits(BigDecimal.valueOf(2)).build());
        verify(creditOrderRepository).deleteById("id");
    }
}
