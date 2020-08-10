package com.oofoodie.backend.command.impl;

import com.blibli.oss.command.CommandExecutor;
import com.oofoodie.backend.command.AddVoucherCommand;
import com.oofoodie.backend.helper.GetRedisData;
import com.oofoodie.backend.models.entity.User;
import com.oofoodie.backend.models.entity.Voucher;
import com.oofoodie.backend.models.request.command.VoucherCommandRequest;
import com.oofoodie.backend.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
public class AddVoucherCommandImpl implements AddVoucherCommand {

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private GetRedisData getRedisData;

    @Override
    public Mono<Voucher> execute(VoucherCommandRequest request) {
        return Mono.zip(storeImg(request.getImage()), getRedisData.getUser(request.getUsername()))
                .map(data -> constructVoucher(data.getT2(), data.getT1(), request))
                .flatMap(voucher -> voucherRepository.save(voucher));
    }

    private Mono<String> storeImg(String img) {
        return commandExecutor.execute(AddImageCommandImpl.class, img)
                .subscribeOn(Schedulers.elastic());
    }

    private Voucher constructVoucher(User user, String img, VoucherCommandRequest request) {
        Voucher voucher = Voucher.builder()
                            .image(img)
                            .name(request.getName())
                            .restaurantId(user.getRestaurantOwner())
                            .build();
        voucher.setId(UUID.randomUUID().toString());

        return voucher;
    }
}
