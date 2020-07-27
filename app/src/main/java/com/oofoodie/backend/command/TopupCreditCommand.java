package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.command.TopupCreditCommandRequest;
import com.oofoodie.backend.models.response.TopupCreditResponse;

public interface TopupCreditCommand extends Command<TopupCreditCommandRequest, TopupCreditResponse> {
}
