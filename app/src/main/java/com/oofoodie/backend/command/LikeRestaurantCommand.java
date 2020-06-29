package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.LikeRequest;
import com.oofoodie.backend.models.response.LikeResponse;

public interface LikeRestaurantCommand extends Command<LikeRequest, LikeResponse> {
}
