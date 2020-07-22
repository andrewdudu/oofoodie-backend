package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.OrderMenuCommandRequest;

public interface OrderMenuCommand extends Command<OrderMenuCommandRequest, Boolean> {
}
