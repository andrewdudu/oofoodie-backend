package com.oofoodie.backend.models.request.command;

import com.oofoodie.backend.validation.VoucherMustExist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteVoucherCommandRequest {

    @NotNull
    @NotBlank
    @VoucherMustExist
    private String voucherId;

    @NotNull
    @NotBlank
    private String username;
}
