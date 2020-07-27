package com.oofoodie.backend.command;

import com.blibli.oss.command.Command;
import com.oofoodie.backend.models.entity.Voucher;

import java.util.List;

public interface GetVoucherCommand extends Command<String, List<Voucher>> {
}
