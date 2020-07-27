package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.GetVoucherCommandImpl;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.entity.Voucher;
import com.oofoodie.backend.repository.VoucherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetVoucherCommandImplTest {

    @InjectMocks
    private GetVoucherCommandImpl command;

    @Mock
    private VoucherRepository voucherRepository;

    @Mock
    private GetRedisData getRedisData;

    @Test
    public void executeTest() {
        List<Voucher> vouchers = Collections.singletonList(new Voucher());
        when(voucherRepository.findAllByRestaurantId("username")).thenReturn(Flux.fromIterable(vouchers));
        when(getRedisData.getUser("username")).thenReturn(Mono.just(User.builder().restaurantOwner("username").build()));

        StepVerifier.create(command.execute("username"))
                .expectNext(vouchers)
                .verifyComplete();

        verify(voucherRepository).findAllByRestaurantId("username");
        verify(getRedisData).getUser("username");
    }
}
