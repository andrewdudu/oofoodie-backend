package com.oofoodie.backend.models.request;

import com.oofoodie.backend.models.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRequest {

    @NotNull
    private String name;

    @NotNull
    private String telephone;

    @NotNull
    private Location location;

    @NotNull
    private String address;

    @NotNull
    private String type;

    @NotNull
    private String cuisine;

    @NotNull
    private String image;

    @NotNull
    private OpenHourRequest openHour;
}
