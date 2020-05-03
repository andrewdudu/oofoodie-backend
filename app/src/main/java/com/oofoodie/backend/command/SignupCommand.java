package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.SignupRequest;
import com.oofoodie.backend.models.response.LoginResponse;

public interface SignupCommand extends Command<SignupRequest, LoginResponse> {
}
