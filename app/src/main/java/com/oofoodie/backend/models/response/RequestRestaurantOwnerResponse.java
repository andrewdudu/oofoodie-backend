package com.oofoodie.backend.models.response;

import com.oofoodie.backend.models.entity.Restaurant;
import com.oofoodie.backend.models.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestRestaurantOwnerResponse {

    private String id;

    private User merchant;

    private Restaurant restaurant;
}
