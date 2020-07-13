package com.oofoodie.backend.validation.validator;

import com.blibli.oss.backend.validation.AbstractReactiveConstraintValidator;
import com.oofoodie.backend.repository.RestaurantRepository;
import com.oofoodie.backend.validation.RestaurantIdMustExists;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintValidatorContext;

public class RestaurantIdMustExistsInDatabase extends AbstractReactiveConstraintValidator<RestaurantIdMustExists, String> {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Mono<Boolean> validate(String restoId, RestaurantIdMustExists annotation, ConstraintValidatorContext context) {
        return restaurantRepository.findById(restoId)
                .map(restaurant -> true)
                .switchIfEmpty(Mono.fromCallable(() -> false));
    }
}
