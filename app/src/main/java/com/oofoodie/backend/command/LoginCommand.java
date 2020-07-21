package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.LoginCommandRequest;
import org.springframework.http.ResponseEntity;

public interface LoginCommand extends Command<LoginCommandRequest, ResponseEntity<?>> {
}
