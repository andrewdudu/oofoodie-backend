package com.oofoodie.backend.command;

import com.oofoodie.backend.command.impl.LikeTimelineCommandImpl;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.Timeline;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.command.LikeTimelineCommandRequest;
import com.oofoodie.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeTimelineCommandImplTest {

    @InjectMocks
    private LikeTimelineCommandImpl command;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GetRedisData getRedisData;

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void executeTest() {
        Timeline timeline = Timeline.builder()
                .likes(new ArrayList<>(Collections.singletonList("username")))
                .build();
        timeline.setId("id");
        User user = User.builder()
                .username("username")
                .timelines(new ArrayList(Collections.singletonList(timeline)))
                .build();

        when(userRepository.findById("id")).thenReturn(Mono.just(user));
        when(userRepository.save(user)).thenReturn(Mono.just(user));
        doNothing().when(getRedisData).saveUser(user);

        StepVerifier.create(command.execute(LikeTimelineCommandRequest.builder()
                    .username("username")
                    .timelineId("id")
                    .targetUserId("id")
                    .build()))
                .expectNext(true)
                .verifyComplete();

        verify(userRepository).findById("id");
        verify(userRepository).save(user);
        verify(getRedisData).saveUser(user);
    }

    @Test
    public void executeTestLikeExist() {
        Timeline timeline = Timeline.builder()
                .likes(new ArrayList<>(Collections.singletonList("user")))
                .build();
        timeline.setId("idd");
        User user = User.builder()
                .username("username")
                .timelines(new ArrayList(Collections.singletonList(timeline)))
                .build();

        when(userRepository.findById("id")).thenReturn(Mono.just(user));
        when(userRepository.save(user)).thenReturn(Mono.just(user));
        doNothing().when(getRedisData).saveUser(user);

        StepVerifier.create(command.execute(LikeTimelineCommandRequest.builder()
                .username("username")
                .timelineId("id")
                .targetUserId("id")
                .build()))
                .expectNext(true)
                .verifyComplete();

        verify(userRepository).findById("id");
        verify(userRepository).save(user);
        verify(getRedisData).saveUser(user);
    }
}
