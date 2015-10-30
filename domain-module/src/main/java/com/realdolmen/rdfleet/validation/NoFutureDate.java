package com.realdolmen.rdfleet.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by JSTAX29 on 30/10/2015.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoFutureDateValidator.class)
public @interface NoFutureDate {
    String message() default "The date was can not be a date in the future.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
