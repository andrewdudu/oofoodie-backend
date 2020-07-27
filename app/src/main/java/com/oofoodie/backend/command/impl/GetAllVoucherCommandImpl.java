package com.oofoodie.backend.command.impl;

import com.oofoodie.backend.command.GetAllVoucherCommand;
import com.oofoodie.backend.models.entity.Voucher;
import com.oofoodie.backend.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GetAllVoucherCommandImpl implements GetAllVoucherCommand {

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public Mono<List<Voucher>> execute(String request) {
        return voucherRepository.findAll()
                .collectList();
    }
}
