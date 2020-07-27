package com.oofoodie.backend.models.request.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopupCreditCommandRequest {

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    private BigDecimal credit;

    @NotNull
    @NotBlank
    private String paymentMethod;
}
