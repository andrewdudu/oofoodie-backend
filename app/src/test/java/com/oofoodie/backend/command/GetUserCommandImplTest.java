package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.GetUserCommandImpl;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetUserCommandImplTest {

    @InjectMocks
    private GetUserCommandImpl command;

    @Mock
    private GetRedisData getRedisData;

    @Test
    public void executeTest() {
        when(getRedisData.getUser("username")).thenReturn(Mono.just(new User()));

        StepVerifier.create(command.execute("username"))
                .expectNext(new User())
                .verifyComplete();

        verify(getRedisData).getUser("username");
    }

    @Test
    public void executeTestBadRequest() {
        when(getRedisData.getUser("username")).thenReturn(Mono.empty());

        StepVerifier.create(command.execute("username"))
                .expectError(BadRequestException.class)
                .verify();

        verify(getRedisData).getUser("username");
    }
}
