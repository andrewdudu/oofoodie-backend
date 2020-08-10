package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.GetAllVoucherCommandImpl;
import com.oofoodie.backend.models.entity.Voucher;
import com.oofoodie.backend.repository.VoucherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllVoucherCommandImplTest {

    @InjectMocks
    private GetAllVoucherCommandImpl command;

    @Mock
    private VoucherRepository voucherRepository;

    @Test
    public void executeTest() {
        List<Voucher> vouchers = Collections.singletonList(new Voucher());
        when(voucherRepository.findAll()).thenReturn(Flux.fromIterable(vouchers));

        StepVerifier.create(command.execute("username"))
                .expectNext(vouchers)
                .verifyComplete();

        verify(voucherRepository).findAll();
    }
}
