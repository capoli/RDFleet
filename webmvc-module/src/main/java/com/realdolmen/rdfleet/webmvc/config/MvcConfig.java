package com.realdolmen.rdfleet.webmvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by cda5732 on 6/10/2015.
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/rd/cars").setViewName("cars");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
    }


}
