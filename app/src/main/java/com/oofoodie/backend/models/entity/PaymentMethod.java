package com.oofoodie.backend.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "payment_method")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod extends BaseEntity {

    @Field(name = "name")
    private String name;

    @Field(name = "image")
    private String image;

    @Field(name = "type")
    private String type;
}
