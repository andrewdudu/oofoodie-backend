package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.GetOrdersCommandRequest;
import com.oofoodie.backend.models.response.OrdersResponse;

import java.util.List;

public interface GetOrdersCommand extends Command<GetOrdersCommandRequest, List<OrdersResponse>> {
}
