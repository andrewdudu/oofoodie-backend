package com.oofoodie.backend.models.request.command;

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
public class VoucherCommandRequest {

    @NotNull
    @NotBlank
    private String image;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String username;
}
