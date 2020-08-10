package com.oofoodie.backend.validation.validator;

import com.blibli.oss.backend.validation.AbstractReactiveConstraintValidator;
import com.oofoodie.backend.repository.TimelineRepository;
import com.oofoodie.backend.validation.TimelineMustExist;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintValidatorContext;

public class TimelineMustExistValidator extends AbstractReactiveConstraintValidator<TimelineMustExist, String> {

    @Autowired
    private TimelineRepository timelineRepository;

    @Override
    public Mono<Boolean> validate(String value, TimelineMustExist annotation, ConstraintValidatorContext context) {
        return timelineRepository.findById(value)
                .map(timeline -> true)
                .switchIfEmpty(Mono.fromCallable(() -> false));
    }
}
