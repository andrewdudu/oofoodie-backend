package com.oofoodie.backend.models.request.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAvailableRestaurantCommandRequest {

    @NotBlank
    private String merchantUsername;
}
