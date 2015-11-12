package com.realdolmen.rdfleet.webmvc.config;

import com.realdolmen.rdfleet.webmvc.converters.DurationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by cda5732 on 6/10/2015.
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login?lang=en").setViewName("login");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DurationConverter());
    }
}
