package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.response.OrdersResponse;

import java.util.List;

public interface GetIncomingOrdersCommand extends Command<String, List<OrdersResponse>> {
}
