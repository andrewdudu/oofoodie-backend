package com.oofoodie.backend.models.request.command;

import com.oofoodie.backend.validation.OrderIdMustExist;
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
public class CancelOrderCommandRequest {

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    @OrderIdMustExist
    private String orderId;
}
