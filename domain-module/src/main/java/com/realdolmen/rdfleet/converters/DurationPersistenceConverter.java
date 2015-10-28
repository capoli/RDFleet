package com.realdolmen.rdfleet.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;

/**
 * Created by JSTAX29 on 28/10/2015.
 */
@Converter(autoApply = true)
public class DurationPersistenceConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        return attribute.toDays();
    }

    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        return Duration.ofDays(dbData);
    }
}
