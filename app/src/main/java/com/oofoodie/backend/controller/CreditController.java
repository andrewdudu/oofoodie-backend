package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.oofoodie.backend.command.PayCommand;
import com.oofoodie.backend.command.TopupCreditCommand;
import com.oofoodie.backend.models.request.PayRequest;
import com.oofoodie.backend.models.request.TopupCreditRequest;
import com.oofoodie.backend.models.request.command.TopupCreditCommandRequest;
import com.oofoodie.backend.models.response.TopupCreditResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class CreditController {

    @Autowired
    private CommandExecutor commandExecutor;

    @PostMapping("/api/merchant/credit")
    public Mono<Response<TopupCreditResponse>> topupCredit(Authentication authentication,
                                                           @RequestBody TopupCreditRequest request) {
        return commandExecutor.execute(TopupCreditCommand.class, constructTopupCreditCommandRequest(authentication, request))
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/api/merchant/pay")
    public Mono<Response<Boolean>> pay(@RequestBody PayRequest request) {
        return commandExecutor.execute(PayCommand.class, request)
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    private TopupCreditCommandRequest constructTopupCreditCommandRequest(Authentication authentication,
                                                                         TopupCreditRequest request) {
        return TopupCreditCommandRequest.builder()
                .credit(request.getCredit())
                .username(authentication.getName())
                .paymentMethod(request.getPaymentMethod())
                .build();
    }
}
