package com.oofoodie.backend.validation;

import com.oofoodie.backend.validation.validator.OrderIdMustExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {OrderIdMustExistValidator.class})
@Documented
public @interface OrderIdMustExist {

    String message() default "does not exist";

    String[] path() default "orderId";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
