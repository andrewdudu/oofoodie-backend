package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.DeleteVoucherCommandImpl;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.entity.Voucher;
import com.oofoodie.backend.models.request.command.DeleteVoucherCommandRequest;
import com.oofoodie.backend.repository.VoucherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteVoucherCommandImplTest {

    @InjectMocks
    private DeleteVoucherCommandImpl command;

    @Mock
    private VoucherRepository voucherRepository;

    @Mock
    private GetRedisData getRedisData;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(getRedisData, voucherRepository);
    }

    @Test
    public void executeTest() {
        when(getRedisData.getUser("username")).thenReturn(Mono.just(User.builder().restaurantOwner("id").build()));
        when(voucherRepository.findById("id")).thenReturn(Mono.just(Voucher.builder().restaurantId("id").build()));
        when(voucherRepository.deleteById("id")).thenReturn(Mono.empty());

        StepVerifier.create(command.execute(DeleteVoucherCommandRequest.builder()
                .voucherId("id")
                .username("username")
                .build()))
                .expectNext(true)
                .verifyComplete();

        verify(getRedisData).getUser("username");
        verify(voucherRepository).findById("id");
        verify(voucherRepository).deleteById("id");
    }

    @Test
    public void executeTestBadRequest() {
        when(getRedisData.getUser("username")).thenReturn(Mono.just(User.builder().restaurantOwner("id").build()));
        when(voucherRepository.findById("id")).thenReturn(Mono.just(new Voucher()));

        StepVerifier.create(command.execute(DeleteVoucherCommandRequest.builder()
                .voucherId("id")
                .username("username")
                .build()))
                .expectError(BadRequestException.class)
                .verify();

        verify(getRedisData).getUser("username");
        verify(voucherRepository).findById("id");
    }
}
