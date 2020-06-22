package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import org.springframework.http.ResponseEntity;

public interface GetImageCommand extends Command<String, ResponseEntity<byte[]>> {
}
