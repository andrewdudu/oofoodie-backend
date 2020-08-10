package com.oofoodie.backend.validation;

import com.oofoodie.backend.validation.validator.PopularRestaurantIsNotDuplicatedInDatabase;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {PopularRestaurantIsNotDuplicatedInDatabase.class})
@Documented
public @interface PopularRestaurantIsNotDuplicated {

    String message() default "you can not promote twice.";

    String[] path() default "restoId";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
