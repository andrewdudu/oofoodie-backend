package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.CancelOrderCommandRequest;

public interface CancelOrderCommand extends Command<CancelOrderCommandRequest, Boolean> {
}
