package com.realdolmen.rdfleet.webmvc;

import com.realdolmen.rdfleet.domain.*;
import com.realdolmen.rdfleet.repositories.RdEmployeeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
@ComponentScan("com.realdolmen.rdfleet")
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Main.class).profiles("production").run();

    }
}