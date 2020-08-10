package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.SignupCommandImpl;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.SignupRequest;
import com.oofoodie.backend.models.response.LoginResponse;
import com.oofoodie.backend.repository.UserRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignupCommandImplTest {

    @InjectMocks
    private SignupCommandImpl command;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @After
    public void after() {
        verifyNoMoreInteractions(passwordEncoder, userRepository);
    }

    @Test
    public void executeTest() {
        when(userRepository.existsByUsernameOrEmail("username", "email")).thenReturn(Mono.just(false));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(new User()));

        StepVerifier.create(command.execute(SignupRequest.builder()
                    .username("username")
                    .email("email")
                    .build()))
                .expectNext(new LoginResponse("User registered successfully", new User()))
                .verifyComplete();

        verify(userRepository).existsByUsernameOrEmail("username", "email");
        verify(userRepository).save(userCaptor.capture());

        assertNotNull(userCaptor.getValue().getId());
    }

    @Test
    public void executeTestBadRequest() {
        when(userRepository.existsByUsernameOrEmail("username", "email")).thenReturn(Mono.just(true));

        StepVerifier.create(command.execute(SignupRequest.builder()
                .username("username")
                .email("email")
                .build()))
                .expectError(BadRequestException.class)
                .verify();

        verify(userRepository).existsByUsernameOrEmail("username", "email");
    }
}
