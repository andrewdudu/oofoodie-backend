package com.oofoodie.backend.models.request;

import com.oofoodie.backend.validation.PopularRestaurantIsNotDuplicated;
import com.oofoodie.backend.validation.RestaurantIdMustExists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopularRestaurantRequest {

    @RestaurantIdMustExists
    @PopularRestaurantIsNotDuplicated
    private String restoId;

    @Min(7)
    @Max(90)
    @NotNull
    private Integer expiredDay;
}
