package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.PayCommand;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.CreditOrder;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.request.PayRequest;
import com.oofoodie.backend.repository.CreditOrderRepository;
import com.oofoodie.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class PayCommandImpl implements PayCommand {

    @Autowired
    private CreditOrderRepository creditOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GetRedisData getRedisData;

    @Override
    public Mono<Boolean> execute(PayRequest request) {
        return creditOrderRepository.findById(request.getCreditOrderId())
                .doOnNext(creditOrder -> addCredit(creditOrder))
                .map(creditOrder -> true)
                .switchIfEmpty(Mono.error(new BadRequestException("Order is expired or Order id is invalid")));

    }

    private void addCredit(CreditOrder creditOrder) {
        getRedisData.getUser(creditOrder.getMerchantUsername())
                .doOnNext(user -> saveUser(user, creditOrder))
                .subscribe();
    }

    private void saveUser(User user, CreditOrder creditOrder) {
        if (Objects.isNull(user.getCredits())) user.setCredits(BigDecimal.ZERO);
        user.setCredits(user.getCredits().add(creditOrder.getCredit()));

        userRepository.save(user)
                .doOnNext(savedUser -> getRedisData.saveUser(user))
                .doOnNext(savedUser -> creditOrderRepository.deleteById(creditOrder.getId()).subscribe())
                .subscribe();
    }
}
