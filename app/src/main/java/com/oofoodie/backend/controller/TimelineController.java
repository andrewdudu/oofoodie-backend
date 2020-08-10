package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.oofoodie.backend.command.LikeTimelineCommand;
import com.oofoodie.backend.models.request.command.LikeTimelineCommandRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class TimelineController {

    @Autowired
    private CommandExecutor commandExecutor;

    @PostMapping("/api/user/timeline/{timelineId}/like/{userId}")
    public Mono<Response<Boolean>> likeTimeline(@PathVariable String timelineId,
                                                @PathVariable String userId,
                                                Authentication authentication) {
        return commandExecutor.execute(LikeTimelineCommand.class, constructLikeTimelineCommandRequest(timelineId, userId, authentication.getName()))
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    private LikeTimelineCommandRequest constructLikeTimelineCommandRequest(String timelineId,
                                                                           String targetUserId,
                                                                           String username) {
        return LikeTimelineCommandRequest.builder()
                .targetUserId(targetUserId)
                .timelineId(timelineId)
                .username(username)
                .build();
    }
}
