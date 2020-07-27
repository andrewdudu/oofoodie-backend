package com.oofoodie.backend.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;

@Document(collection = "credit_order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditOrder extends BaseEntity {

    @Field(name = "merchant_username")
    private String merchantUsername;

    @Field(name = "credit")
    private BigDecimal credit;

    @Field(name = "payment_method")
    private String paymentMethod;

    @Field
    @Indexed(name = "expiredDateInSeconds", expireAfterSeconds = 3600)
    private Date expiredDateInSeconds;
}
