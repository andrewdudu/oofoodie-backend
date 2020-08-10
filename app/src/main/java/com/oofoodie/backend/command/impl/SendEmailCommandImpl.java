package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.SendEmailCommand;
import com.oofoodie.backend.models.request.SendEmailRequest;
import com.oofoodie.backend.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SendEmailCommandImpl implements SendEmailCommand {

    @Autowired
    private MailService mailService;

    @Override
    public Mono<Boolean> execute(SendEmailRequest request) {
        return Mono.fromCallable(() -> mailService.sendEmail(request.getEmail(), request.getMessage()));
    }
}
