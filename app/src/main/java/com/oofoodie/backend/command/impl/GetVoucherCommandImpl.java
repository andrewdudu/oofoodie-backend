package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.GetVoucherCommand;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.Voucher;
import com.oofoodie.backend.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GetVoucherCommandImpl implements GetVoucherCommand {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private GetRedisData getRedisData;

    @Override
    public Mono<List<Voucher>> execute(String request) {
        return getRedisData.getUser(request)
                .flatMap(user -> voucherRepository.findAllByRestaurantId(user.getRestaurantOwner())
                    .collectList());
    }
}
