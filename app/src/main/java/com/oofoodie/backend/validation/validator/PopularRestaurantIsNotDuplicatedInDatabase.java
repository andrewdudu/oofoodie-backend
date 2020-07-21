package com.oofoodie.backend.validation.validator;

import com.blibli.oss.backend.validation.AbstractReactiveConstraintValidator;
import com.oofoodie.backend.repository.PopularRestaurantRepository;
import com.oofoodie.backend.validation.PopularRestaurantIsNotDuplicated;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintValidatorContext;

public class PopularRestaurantIsNotDuplicatedInDatabase extends AbstractReactiveConstraintValidator<PopularRestaurantIsNotDuplicated, String> {

    @Autowired
    private PopularRestaurantRepository popularRestaurantRepository;

    @Override
    public Mono<Boolean> validate(String restoId, PopularRestaurantIsNotDuplicated annotation, ConstraintValidatorContext context) {
        return popularRestaurantRepository.findByRestoId(restoId)
                .map(popularRestaurant -> false)
                .switchIfEmpty(Mono.fromCallable(() -> true));
    }
}
