package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.MailCommandImpl;
import com.oofoodie.backend.models.request.MailRequest;
import com.oofoodie.backend.service.MailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MailCommandImplTest {

    @InjectMocks
    private MailCommandImpl command;

    @Mock
    private MailService mailService;

    @Test
    public void executeTest() {
        when(mailService.sendForgotPasswordMail("email", "message")).thenReturn(true);

        StepVerifier.create(command.execute(new MailRequest("email", "message")))
                .expectNext(true)
                .verifyComplete();

        verify(mailService).sendForgotPasswordMail("email", "message");
    }
}
