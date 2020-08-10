package com.oofoodie.backend.controller;

import com.blibli.oss.command.CommandExecutor;
import com.blibli.oss.common.response.Response;
import com.blibli.oss.common.response.ResponseHelper;
import com.oofoodie.backend.command.AddVoucherCommand;
import com.oofoodie.backend.command.DeleteVoucherCommand;
import com.oofoodie.backend.command.GetAllVoucherCommand;
import com.oofoodie.backend.command.GetVoucherCommand;
import com.oofoodie.backend.models.entity.Voucher;
import com.oofoodie.backend.models.request.VoucherRequest;
import com.oofoodie.backend.models.request.command.DeleteVoucherCommandRequest;
import com.oofoodie.backend.models.request.command.VoucherCommandRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
public class VoucherController {

    @Autowired
    private CommandExecutor commandExecutor;

    @GetMapping("/api/voucher")
    public Mono<Response<List<Voucher>>> getAllVoucher() {
        return commandExecutor.execute(GetAllVoucherCommand.class, "")
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @PostMapping("/api/merchant/voucher")
    public Mono<Response<Voucher>> addVoucher(@RequestBody VoucherRequest request,
                                              Authentication authentication) {
        return commandExecutor.execute(AddVoucherCommand.class, constructVoucherCommandRequest(request, authentication))
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @GetMapping("/api/merchant/voucher")
    public Mono<Response<List<Voucher>>> getVoucher(Authentication authentication) {
        return commandExecutor.execute(GetVoucherCommand.class, authentication.getName())
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    @DeleteMapping("/api/merchant/voucher/{voucherId}")
    public Mono<Response<Boolean>> deleteVoucher(Authentication authentication, @PathVariable String voucherId) {
        return commandExecutor.execute(DeleteVoucherCommand.class, constructDeleteVoucherCommandRequest(authentication, voucherId))
                .map(ResponseHelper::ok)
                .subscribeOn(Schedulers.elastic());
    }

    private DeleteVoucherCommandRequest constructDeleteVoucherCommandRequest(Authentication authentication, String voucherId) {
        return DeleteVoucherCommandRequest.builder()
                .username(authentication.getName())
                .voucherId(voucherId)
                .build();
    }

    private VoucherCommandRequest constructVoucherCommandRequest(VoucherRequest request, Authentication authentication) {
        return VoucherCommandRequest.builder()
                .image(request.getImage())
                .name(request.getName())
                .username(authentication.getName())
                .build();
    }
}
