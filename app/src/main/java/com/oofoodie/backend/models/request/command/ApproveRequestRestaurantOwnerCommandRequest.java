package com.oofoodie.backend.models.request.command;

import com.oofoodie.backend.validation.RestaurantRequestMustExists;
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
public class ApproveRequestRestaurantOwnerCommandRequest {

    @NotNull
    @NotBlank
    @RestaurantRequestMustExists
    private String requestId;
}
