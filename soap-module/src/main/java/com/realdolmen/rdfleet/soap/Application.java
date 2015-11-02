package com.realdolmen.rdfleet.soap;

import com.realdolmen.rdfleet.domain.*;
import com.realdolmen.rdfleet.repositories.EmployeeCarRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;

/**
 * Created by JSTAX29 on 28/10/2015.
 */
@SpringBootApplication
@ComponentScan("com.realdolmen.rdfleet")
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class).profiles("production").run();

    }
}
