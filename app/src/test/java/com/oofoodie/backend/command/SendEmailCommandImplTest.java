package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.SendEmailCommandImpl;
import com.oofoodie.backend.models.request.SendEmailRequest;
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
public class SendEmailCommandImplTest {

    @InjectMocks
    private SendEmailCommandImpl command;

    @Mock
    private MailService mailService;

    @Test
    public void executeTest() {
        when(mailService.sendEmail("email", "message")).thenReturn(true);
        StepVerifier.create(command.execute(new SendEmailRequest("email", "message")))
                .expectNext(true)
                .verifyComplete();

        verify(mailService).sendEmail("email", "message");
    }
}
