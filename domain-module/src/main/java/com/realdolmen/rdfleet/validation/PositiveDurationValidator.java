package com.realdolmen.rdfleet.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

/**
 * Created by JSTAX29 on 30/10/2015.
 */
public class PositiveDurationValidator implements ConstraintValidator<PositiveDuration, Duration> {
    @Override
    public void initialize(PositiveDuration constraintAnnotation) {

    }

    @Override
    public boolean isValid(Duration value, ConstraintValidatorContext context) {
        return (value != null && value.toDays() >= 0);
    }
}
