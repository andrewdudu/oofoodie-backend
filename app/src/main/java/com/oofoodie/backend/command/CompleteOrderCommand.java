package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.CancelOrderCommandRequest;

public interface CompleteOrderCommand extends Command<CancelOrderCommandRequest, Boolean> {
}
