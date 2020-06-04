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
public class RestaurantLocationRequest {

    @NotNull
    private String restoId;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;
}
