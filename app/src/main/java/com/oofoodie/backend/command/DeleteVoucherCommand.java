package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.DeleteVoucherCommandRequest;

public interface DeleteVoucherCommand extends Command<DeleteVoucherCommandRequest, Boolean> {
}
