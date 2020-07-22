package com.oofoodie.backend.validation.validator;

import com.blibli.oss.backend.validation.AbstractReactiveConstraintValidator;
import com.oofoodie.backend.repository.RestaurantOwnerRequestRepository;
import com.oofoodie.backend.validation.RestaurantRequestMustExists;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintValidatorContext;

public class RestaurantRequestMustExistsInDatabase extends AbstractReactiveConstraintValidator<RestaurantRequestMustExists, String> {

    @Autowired
    private RestaurantOwnerRequestRepository restaurantOwnerRequestRepository;

    @Override
    public Mono<Boolean> validate(String value, RestaurantRequestMustExists annotation, ConstraintValidatorContext context) {
        return restaurantOwnerRequestRepository.findById(value)
                .map(restaurantOwnerRequest -> true)
                .switchIfEmpty(Mono.just(false));
    }
}
