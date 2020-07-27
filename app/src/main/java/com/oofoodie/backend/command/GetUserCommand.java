package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.entity.User;

public interface GetUserCommand extends Command<String, User> {
}
