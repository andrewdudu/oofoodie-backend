package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.entity.Voucher;
import com.oofoodie.backend.models.request.command.VoucherCommandRequest;

public interface AddVoucherCommand extends Command<VoucherCommandRequest, Voucher> {
}
