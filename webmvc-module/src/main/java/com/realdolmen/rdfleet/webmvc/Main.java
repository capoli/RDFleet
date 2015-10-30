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

/**
 * Created by JSTAX29 on 28/10/2015.
 */
@SpringBootApplication
@ComponentScan("com.realdolmen.rdfleet")
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Main.class).profiles("production").run();

        Car car = new Car();
        car.setFunctionalLevel(5);
        car.setMake("Audi");
        car.setModel("A3");
        car.setAmountDowngrade(BigDecimal.valueOf(2315.25));
        car.setFuelType(FuelType.DIESEL);
        car.setAmountUpgrade(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_UP));
        car.setListPrice(BigDecimal.valueOf(25343.22));
        car.setTyreType(TyreType.ALUMINIUM);
        car.setTimeOfDeliveryInDays(Duration.ofDays(150));
        car.setBenefit(BigDecimal.valueOf(158.58));

        EmployeeCar employeeCar = new EmployeeCar();
        employeeCar.setSelectedCar(car);
        CarOption a = new CarOption();
        a.setDescription("Trekhaak");
        CarOption b = new CarOption();
        b.setDescription("Parkeersensor");
        employeeCar.setCarOptions(Arrays.asList(a, b));
        employeeCar.setCarStatus(CarStatus.IN_USE);
        employeeCar.setLicensePlate("1-FGH-468");

        Order order = new Order();
        order.setAmountPaidByCompany(BigDecimal.valueOf(24000));
        order.setAmountPaidByEmployee(BigDecimal.valueOf(25343.22));
        order.setDateOrdered(LocalDate.of(2012, 5, 5));
        order.setDateReceived(LocalDate.of(2012, 8, 3));
        order.setOrderedCar(employeeCar);

        RdEmployee rdEmployee = new RdEmployee();
        rdEmployee.setInService(true);
        rdEmployee.setFunctionalLevel(4);
        rdEmployee.setFirstName("Fleet");
        rdEmployee.setLastName("Realdolmen");
        rdEmployee.setEmail("fleet.realdolmen@gmail.com");
        rdEmployee.setPassword("password");
        rdEmployee.setCurrentOrder(order);

        RdEmployeeRepository bean = context.getBean(RdEmployeeRepository.class);
        bean.save(rdEmployee);
    }
}