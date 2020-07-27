package com.oofoodie.backend.command;

import com.blibli.oss.command.CommandExecutor;
import com.oofoodie.backend.command.impl.AddImageCommandImpl;
import com.oofoodie.backend.command.impl.AddVoucherCommandImpl;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.entity.Voucher;
import com.oofoodie.backend.models.request.command.VoucherCommandRequest;
import com.oofoodie.backend.repository.VoucherRepository;
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
public class AddVoucherCommandImplTest {

    @InjectMocks
    private AddVoucherCommandImpl command;

    @Mock
    private CommandExecutor commandExecutor;

    @Mock
    private VoucherRepository voucherRepository;

    @Mock
    private GetRedisData getRedisData;

    @Captor
    private ArgumentCaptor<Voucher> voucherCaptor;

    @After
    public void after() {
        verifyNoMoreInteractions(voucherRepository, commandExecutor, getRedisData);
    }

    @Test
    public void executeTest() {
        when(commandExecutor.execute(AddImageCommandImpl.class, "image")).thenReturn(Mono.just("image"));
        when(getRedisData.getUser("username")).thenReturn(Mono.just(new User()));
        when(voucherRepository.save(any(Voucher.class))).thenReturn(Mono.just(new Voucher()));

        StepVerifier.create(command.execute(VoucherCommandRequest.builder()
                    .username("username")
                    .image("image")
                    .build()))
                .expectNext(new Voucher())
                .verifyComplete();

        verify(commandExecutor).execute(AddImageCommandImpl.class, "image");
        verify(getRedisData).getUser("username");
        verify(voucherRepository).save(voucherCaptor.capture());

        assertEquals("image", voucherCaptor.getValue().getImage());
    }
}
