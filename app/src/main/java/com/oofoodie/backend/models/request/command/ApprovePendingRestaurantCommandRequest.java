package com.oofoodie.backend.models.request.command;

import com.oofoodie.backend.validation.RestaurantIdMustExists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovePendingRestaurantCommandRequest {

    @RestaurantIdMustExists
    private String restaurantId;
}
