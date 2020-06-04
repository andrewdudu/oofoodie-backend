package com.oofoodie.backend.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenHourRequest {

    @NotNull
    private Hour sunday;

    @NotNull
    private Hour monday;

    @NotNull
    private Hour tuesday;

    @NotNull
    private Hour wednesday;

    @NotNull
    private Hour thursday;

    @NotNull
    private Hour friday;

    @NotNull
    private Hour saturday;
}
