package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.oofoodie.backend.command.impl.GetImageCommandImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
public class FileController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping("/api/img/{fileName}")
    public Mono<ResponseEntity<byte[]>> getImage(@PathVariable String fileName) throws IOException {
        return commandExecutor.execute(GetImageCommandImpl.class, fileName);
    }
}
