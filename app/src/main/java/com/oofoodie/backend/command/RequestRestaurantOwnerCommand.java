package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.RequestRestaurantOwnerCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;

public interface RequestRestaurantOwnerCommand extends Command<RequestRestaurantOwnerCommandRequest, RestaurantResponse> {
}
