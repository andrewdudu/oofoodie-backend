package com.oofoodie.backend.validation.validator;

import com.blibli.oss.backend.validation.AbstractReactiveConstraintValidator;
import com.oofoodie.backend.repository.UserRepository;
import com.oofoodie.backend.validation.UserMustExist;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintValidatorContext;

public class UserMustExistValidator extends AbstractReactiveConstraintValidator<UserMustExist, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<Boolean> validate(String value, UserMustExist annotation, ConstraintValidatorContext context) {
        return userRepository.findById(value)
                .map(timeline -> true)
                .switchIfEmpty(Mono.fromCallable(() -> false));
    }
}
