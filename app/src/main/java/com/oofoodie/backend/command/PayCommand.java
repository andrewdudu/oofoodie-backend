package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.PayRequest;

public interface PayCommand extends Command<PayRequest, Boolean> {
}
