package com.oofoodie.backend.validation.validator;

import com.blibli.oss.backend.validation.AbstractReactiveConstraintValidator;
import com.oofoodie.backend.repository.OrderRepository;
import com.oofoodie.backend.validation.OrderIdMustExist;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintValidatorContext;

public class OrderIdMustExistValidator extends AbstractReactiveConstraintValidator<OrderIdMustExist, String> {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Mono<Boolean> validate(String value, OrderIdMustExist annotation, ConstraintValidatorContext context) {
        return orderRepository.findById(value)
                .map(order -> true)
                .switchIfEmpty(Mono.fromCallable(() -> false));
    }
}
