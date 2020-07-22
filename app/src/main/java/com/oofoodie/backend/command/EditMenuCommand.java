package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.EditMenuCommandRequest;
import com.oofoodie.backend.models.response.MenuResponse;

import java.util.List;

public interface EditMenuCommand extends Command<EditMenuCommandRequest, List<MenuResponse>> {
}
