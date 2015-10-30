package com.realdolmen.rdfleet;

import com.realdolmen.rdfleet.domain.*;
import com.realdolmen.rdfleet.repositories.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;

/**
 * Created by JSTAX29 on 28/10/2015.
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Main.class).profiles("production").run();
//        Car car = new Car();
//        car.setFunctionalLevel(2);
//        car.setMake("Audi");
//        car.setModel("A1");
//        car.setAmountDowngrade(BigDecimal.valueOf(2315.25));
//        car.setFuelType(FuelType.DIESEL);
//        car.setAmountUpgrade(BigDecimal.valueOf(0));
//        car.setListPrice(BigDecimal.valueOf(25343.22));
//        car.setTyreType(TyreType.ALUMINIUM);
//        car.setTimeOfDeliveryInDays(Duration.ofDays(150));
//
//        EmployeeCar employeeCar = new EmployeeCar();
//        employeeCar.setSelectedCar(car);
//        employeeCar.setLicensePlate("1-HLR-743");
//        CarOption a = new CarOption();
//        a.setDescription("Trekhaak");
//        CarOption b = new CarOption();
//        b.setDescription("Parkeersensor");
//        employeeCar.setCarOptions(Arrays.asList(a, b));
//        employeeCar.setCarStatus(CarStatus.NOT_USED);
//
//        EmployeeCarRepository employeeCarRepository = context.getBean(EmployeeCarRepository.class);
//        employeeCarRepository.save(employeeCar);
    }
}