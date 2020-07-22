package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.GetAvailableRestaurantCommandRequest;
import com.oofoodie.backend.models.response.RestaurantResponse;

import java.util.List;

public interface GetAvailableRestaurantCommand extends Command<GetAvailableRestaurantCommandRequest, List<RestaurantResponse>> {
}
