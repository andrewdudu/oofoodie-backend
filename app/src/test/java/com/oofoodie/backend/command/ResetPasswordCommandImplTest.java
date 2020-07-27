package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.ResetPasswordCommandImpl;
import com.oofoodie.backend.handler.TokenProvider;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.ResetPasswordRequest;
import com.oofoodie.backend.repository.UserRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResetPasswordCommandImplTest {

    @InjectMocks
    private ResetPasswordCommandImpl command;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisTemplate<Object, Object> redisTemplate;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GetRedisData getRedisData;

    @After
    public void after() {
        verifyNoMoreInteractions(passwordEncoder, redisTemplate, userRepository, tokenProvider);
    }

    @Test
    public void executeTest() {
        User user = User.builder()
                .username("username")
                .email("email")
                .password("password")
                .build();

        when(tokenProvider.getEmailFromToken("token")).thenReturn("email");
        when(redisTemplate.hasKey("reset-password-email")).thenReturn(true);
        when(userRepository.findByEmail("email")).thenReturn(Mono.just(user));
        when(passwordEncoder.encode("password")).thenReturn("password");
        when(userRepository.save(user)).thenReturn(Mono.just(user));
        doNothing().when(getRedisData).saveUser(user);
        doNothing().when(getRedisData).deleteResetToken("email");

        StepVerifier.create(command.execute(ResetPasswordRequest.builder()
                    .password("password")
                    .token("token")
                    .build()))
                .expectNext("OK")
                .verifyComplete();

        verify(tokenProvider).getEmailFromToken("token");
        verify(redisTemplate).hasKey("reset-password-email");
        verify(userRepository).findByEmail("email");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(user);
        verify(getRedisData).saveUser(user);
        verify(getRedisData).deleteResetToken("email");
    }
}
