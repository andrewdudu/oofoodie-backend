package com.oofoodie.backend.validation;

import com.oofoodie.backend.validation.validator.RestaurantRequestMustExistsInDatabase;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {RestaurantRequestMustExistsInDatabase.class})
@Documented
public @interface RestaurantRequestMustExists {

    String message() default "Already owned restaurant";

    String[] path() default "merchant";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
