package com.oofoodie.backend.models.request.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeTimelineCommandRequest {

    @NotNull
    @NotBlank
    private String timelineId;

    @NotNull
    @NotBlank
    private String targetUserId;

    @NotNull
    private String username;
}
