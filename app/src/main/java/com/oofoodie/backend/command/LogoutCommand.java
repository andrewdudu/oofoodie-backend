package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import org.springframework.http.ResponseEntity;

public interface LogoutCommand extends Command<String, ResponseEntity<?>> {
}
