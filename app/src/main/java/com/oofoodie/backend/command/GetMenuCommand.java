package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.GetMenuCommandRequest;
import com.oofoodie.backend.models.response.MenuResponse;

import java.util.List;

public interface GetMenuCommand extends Command<GetMenuCommandRequest, List<MenuResponse>> {
}
