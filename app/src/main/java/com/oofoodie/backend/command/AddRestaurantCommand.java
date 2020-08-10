package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.AddRestaurantCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;

public interface AddRestaurantCommand extends Command<AddRestaurantCommandRequest, RestaurantResponse> {
}
