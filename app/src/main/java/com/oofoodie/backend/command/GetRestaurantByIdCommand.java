package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.response.RestaurantResponse;

public interface GetRestaurantByIdCommand extends Command<String, RestaurantResponse> {
}
