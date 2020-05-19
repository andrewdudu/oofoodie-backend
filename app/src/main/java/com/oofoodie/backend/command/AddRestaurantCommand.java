package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.RestaurantRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;

public interface AddRestaurantCommand extends Command<RestaurantRequest, RestaurantResponse> {
}
