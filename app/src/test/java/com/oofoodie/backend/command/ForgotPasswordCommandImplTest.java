package com.oofoodie.backend.command;

import com.blibli.oss.command.CommandExecutor;
import com.oofoodie.backend.command.impl.ForgotPasswordCommandImpl;
import com.oofoodie.backend.command.impl.MailCommandImpl;
import com.oofoodie.backend.handler.TokenProvider;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.ForgotPasswordRequest;
import com.oofoodie.backend.models.request.MailRequest;
import com.oofoodie.backend.models.response.ForgotPasswordResponse;
import com.oofoodie.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ForgotPasswordCommandImplTest {

    @InjectMocks
    private ForgotPasswordCommandImpl command;

    @Mock
    private CommandExecutor commandExecutor;

    @Mock
    private RedisTemplate<Object, Object> redisTemplate;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private GetRedisData getRedisData;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(tokenProvider, userRepository, redisTemplate, commandExecutor);
    }

    @Test
    public void executeTest() {
        User user = User.builder()
                .email("email")
                .build();
        when(userRepository.findByEmail("email")).thenReturn(Mono.just(user));
        when(redisTemplate.hasKey("reset-password-email")).thenReturn(true);
        when(getRedisData.get("reset-password-email")).thenReturn("token");
        when(commandExecutor.execute(eq(MailCommandImpl.class), any(MailRequest.class))).thenReturn(Mono.just(true));

        StepVerifier.create(command.execute(ForgotPasswordRequest.builder()
                    .email("email")
                    .build()))
                .expectNext(new ForgotPasswordResponse("token"))
                .verifyComplete();

        verify(userRepository).findByEmail("email");
        verify(redisTemplate).hasKey("reset-password-email");
        verify(getRedisData).get("reset-password-email");
        verify(commandExecutor).execute(eq(MailCommandImpl.class), any(MailRequest.class));
    }

    @Test
    public void executeTestNoRedisData() {
        User user = User.builder()
                .email("email")
                .build();
        when(userRepository.findByEmail("email")).thenReturn(Mono.just(user));
        when(redisTemplate.hasKey("reset-password-email")).thenReturn(false);
        when(tokenProvider.generatePasswordResetToken("email")).thenReturn("token");
        doNothing().when(getRedisData).setResetPasswordToken("reset-password-email", "token");
        when(commandExecutor.execute(eq(MailCommandImpl.class), any(MailRequest.class))).thenReturn(Mono.just(true));

        StepVerifier.create(command.execute(ForgotPasswordRequest.builder()
                .email("email")
                .build()))
                .expectNext(new ForgotPasswordResponse("token"))
                .verifyComplete();

        verify(userRepository).findByEmail("email");
        verify(redisTemplate).hasKey("reset-password-email");
        verify(tokenProvider).generatePasswordResetToken("email");
        verify(getRedisData).setResetPasswordToken("reset-password-email", "token");
        verify(commandExecutor).execute(eq(MailCommandImpl.class), any(MailRequest.class));
    }
}
