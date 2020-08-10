package com.oofoodie.backend.models.request.command;

import com.oofoodie.backend.models.entity.Menu;
import com.oofoodie.backend.validation.RestaurantIdMustExists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditMenuCommandRequest {

    @NotNull
    @NotBlank
    @RestaurantIdMustExists
    private String restaurantId;

    @NotNull
    @NotBlank
    private String merchantUsername;

    @NotNull
    private List<Menu> menu;
}
