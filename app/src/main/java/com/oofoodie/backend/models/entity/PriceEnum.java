package com.oofoodie.backend.models.entity;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceEnum {

    private Integer days;

    private BigDecimal price;
}
