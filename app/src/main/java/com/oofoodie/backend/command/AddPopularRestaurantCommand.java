package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.PopularRestaurantRequest;
import com.oofoodie.backend.models.response.PopularRestaurantResponse;

public interface AddPopularRestaurantCommand extends Command<PopularRestaurantRequest, PopularRestaurantResponse> {
}
