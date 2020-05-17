package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.ResetPasswordRequest;

public interface ResetPasswordCommand extends Command<ResetPasswordRequest, String> {
}
