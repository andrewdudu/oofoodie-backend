package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.request.ReviewRequest;
import com.oofoodie.backend.models.response.ReviewResponse;

public interface AddReviewCommand extends Command<ReviewRequest, ReviewResponse> {
}
