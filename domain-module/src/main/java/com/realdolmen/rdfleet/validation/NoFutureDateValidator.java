package com.realdolmen.rdfleet.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/**
 * Created by JSTAX29 on 30/10/2015.
 */
public class NoFutureDateValidator implements ConstraintValidator<NoFutureDate, LocalDate> {
    @Override
    public void initialize(NoFutureDate constraintAnnotation) {

    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || !value.isAfter(LocalDate.now());
    }
}
