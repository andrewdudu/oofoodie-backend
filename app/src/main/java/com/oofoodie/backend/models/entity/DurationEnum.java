package com.oofoodie.backend.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum DurationEnum {
    ONE_WEEK(PriceEnum.builder().days(7).price(BigDecimal.valueOf(30000)).build()),
    TWO_WEEK(PriceEnum.builder().days(14).price(BigDecimal.valueOf(50000)).build()),
    ONE_MONTH(PriceEnum.builder().days(30).price(BigDecimal.valueOf(80000)).build());

    private final PriceEnum value;

    public PriceEnum getValue() {
        return this.value;
    }
}
