package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.ApprovePendingRestaurantCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;

public interface ApprovePendingRestaurantCommand extends Command<ApprovePendingRestaurantCommandRequest, RestaurantResponse> {
}
