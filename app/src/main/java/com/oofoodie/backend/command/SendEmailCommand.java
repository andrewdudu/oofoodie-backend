package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.SendEmailRequest;

public interface SendEmailCommand extends Command<SendEmailRequest, Boolean> {
}
