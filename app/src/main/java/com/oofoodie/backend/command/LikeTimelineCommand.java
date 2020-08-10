package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.LikeTimelineCommandRequest;

public interface LikeTimelineCommand extends Command<LikeTimelineCommandRequest, Boolean> {
}
