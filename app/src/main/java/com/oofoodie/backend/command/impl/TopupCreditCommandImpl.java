package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.TopupCreditCommand;
import com.oofoodie.backend.models.entity.CreditOrder;
import com.oofoodie.backend.models.request.command.TopupCreditCommandRequest;
import com.oofoodie.backend.models.response.TopupCreditResponse;
import com.oofoodie.backend.repository.CreditOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class TopupCreditCommandImpl implements TopupCreditCommand {

    @Autowired
    private CreditOrderRepository creditOrderRepository;

    @Override
    public Mono<TopupCreditResponse> execute(TopupCreditCommandRequest request) {
        return creditOrderRepository.save(newCreditOrder(request))
                .map(this::constructTopupCreditResponse);
    }

    private TopupCreditResponse constructTopupCreditResponse(CreditOrder creditOrder) {
        return TopupCreditResponse.builder()
                .orderNumber(creditOrder.getId())
                .build();
    }

    private CreditOrder newCreditOrder(TopupCreditCommandRequest request) {
        CreditOrder creditOrder = CreditOrder.builder()
                .credit(request.getCredit())
                .merchantUsername(request.getUsername())
                .paymentMethod(request.getPaymentMethod())
                .expiredDateInSeconds(Date.from(Instant.now()))
                .build();
        creditOrder.setId(UUID.randomUUID().toString());

        return creditOrder;
    }
}
