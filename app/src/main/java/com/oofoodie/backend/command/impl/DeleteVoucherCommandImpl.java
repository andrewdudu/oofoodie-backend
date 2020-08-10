package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.DeleteVoucherCommand;
import com.oofoodie.backend.exception.BadRequestException;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.entity.Voucher;
import com.oofoodie.backend.models.request.command.DeleteVoucherCommandRequest;
import com.oofoodie.backend.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteVoucherCommandImpl implements DeleteVoucherCommand {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private GetRedisData getRedisData;

    @Override
    public Mono<Boolean> execute(DeleteVoucherCommandRequest request) {
        return Mono.zip(getRedisData.getUser(request.getUsername()), voucherRepository.findById(request.getVoucherId()))
                .flatMap(data -> validateUser(data.getT1(), data.getT2()))
                .doOnNext(bool -> voucherRepository.deleteById(request.getVoucherId()).subscribe())
                .map(bool -> true);
    }

    private Mono<Boolean> validateUser(User user, Voucher voucher) {
        if (!user.getRestaurantOwner().equals(voucher.getRestaurantId())) {
            return Mono.error(new BadRequestException("you do not own the voucher."));
        }

        return Mono.just(true);
    }
}
