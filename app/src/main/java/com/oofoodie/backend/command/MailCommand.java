package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.MailRequest;

public interface MailCommand extends Command<MailRequest, Boolean> {
}
