package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.MailCommand;
import com.oofoodie.backend.models.request.MailRequest;
import com.oofoodie.backend.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MailCommandImpl implements MailCommand {

    @Autowired
    private MailService mailService;

    @Override
    public Mono<Boolean> execute(MailRequest request) {
        return Mono.fromCallable(() -> mailService.sendForgotPasswordMail(request.getEmail(), request.getToken()));
    }
}
