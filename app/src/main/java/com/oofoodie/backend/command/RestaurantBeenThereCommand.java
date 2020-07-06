package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.BeenThereCommandRequest;
import com.oofoodie.backend.models.response.LikeResponse;

public interface RestaurantBeenThereCommand extends Command<BeenThereCommandRequest, LikeResponse> {
}
