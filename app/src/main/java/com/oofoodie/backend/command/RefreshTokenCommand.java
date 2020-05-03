package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.RefreshRequest;
import org.springframework.http.ResponseEntity;

public interface RefreshTokenCommand extends Command<RefreshRequest, ResponseEntity<?>> {
}
