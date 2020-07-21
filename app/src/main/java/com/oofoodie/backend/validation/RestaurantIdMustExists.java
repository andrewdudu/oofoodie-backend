package com.oofoodie.backend.validation;

import com.oofoodie.backend.validation.validator.RestaurantIdMustExistsInDatabase;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {RestaurantIdMustExistsInDatabase.class})
@Documented
public @interface RestaurantIdMustExists {

    String message() default "Does not exists";

    String[] path() default "restoId";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
