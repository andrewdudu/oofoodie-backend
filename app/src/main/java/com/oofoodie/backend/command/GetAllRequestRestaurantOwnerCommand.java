package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.response.RequestRestaurantOwnerResponse;

import java.util.List;

public interface GetAllRequestRestaurantOwnerCommand extends Command<String, List<RequestRestaurantOwnerResponse>> {
}
