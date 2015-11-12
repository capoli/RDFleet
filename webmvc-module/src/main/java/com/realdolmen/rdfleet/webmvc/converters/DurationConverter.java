package com.realdolmen.rdfleet.webmvc.converters;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Duration;
import java.util.Locale;

public class DurationConverter implements Formatter<Duration> {
    @Override
    public Duration parse(String s, Locale locale) throws ParseException {
        Long days = Long.parseLong(s);
        return Duration.ofDays(days);
    }

    @Override
    public String print(Duration duration, Locale locale) {
        return Long.toString(duration.toDays());
    }
}
