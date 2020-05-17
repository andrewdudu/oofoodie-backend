package com.oofoodie.backend.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document(collection = "credit_payment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditPayment extends BaseEntity {

    @Field(name = "credit")
    private Integer credit;

    @Field(name = "price")
    private BigDecimal price;

    @Field(name = "payment_method")
    private PaymentMethod paymentMethod;
}
