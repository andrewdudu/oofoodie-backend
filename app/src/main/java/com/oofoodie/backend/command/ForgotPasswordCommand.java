package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.ForgotPasswordRequest;
import com.oofoodie.backend.models.response.ForgotPasswordResponse;

public interface ForgotPasswordCommand extends Command<ForgotPasswordRequest, ForgotPasswordResponse> {
}
