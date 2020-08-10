package com.oofoodie.backend.models.request;

import com.oofoodie.backend.models.entity.DurationEnum;
import com.oofoodie.backend.validation.PopularRestaurantIsNotDuplicated;
import com.oofoodie.backend.validation.RestaurantIdMustExists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularRestaurantRequest {

    @RestaurantIdMustExists
    @PopularRestaurantIsNotDuplicated
    private String restoId;

    @NotNull
    private DurationEnum expiredDay;
}
