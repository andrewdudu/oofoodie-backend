package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.LikeTimelineCommand;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.Timeline;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.command.LikeTimelineCommandRequest;
import com.oofoodie.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LikeTimelineCommandImpl implements LikeTimelineCommand {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GetRedisData getRedisData;

    @Override
    public Mono<Boolean> execute(LikeTimelineCommandRequest request) {
        return getUserAndAddLike(request)
                .map(user -> true);
    }

    private Mono<User> getUserAndAddLike(LikeTimelineCommandRequest request) {
        return userRepository.findById(request.getTargetUserId())
                .flatMap(user -> addLike(user, request));
    }

    private Mono<User> addLike(User user, LikeTimelineCommandRequest request) {
        List<Timeline> timelines = user.getTimelines();
        if (Objects.isNull(timelines)) {
            timelines = new ArrayList<>();
        }
        Timeline notEdited = Timeline.builder()
                .likes(new ArrayList<>())
                .build();
        Timeline timeline = Timeline.builder()
                .likes(new ArrayList<>())
                .build();

        for (Timeline tl : user.getTimelines()) {
            if (tl.getId().equals(request.getTimelineId())) {
                timeline = tl;
                notEdited = tl;
            }
        }

        List<String> likes = timeline.getLikes();

        if (likes.contains(request.getUsername())) {
            likes.remove(request.getUsername());
        } else {
            likes.add(request.getUsername());
        }

        timeline.setLikes(likes);
        if (timelines.size() != 0) timelines.remove(notEdited);
        timelines.add(timeline);
        user.setTimelines(timelines);

        return userRepository.save(user)
                .doOnNext(savedUser -> getRedisData.saveUser(savedUser));
    }
}
