package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.response.RestaurantResponse;

import java.util.List;

public interface GetPopularRestaurantCommand extends Command<String, List<RestaurantResponse>> {
}
