package com.realdolmen.rdfleet.scheduling.util;

import com.realdolmen.rdfleet.domain.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Created by JSTAX29 on 29/10/2015.
 * A reusable utility class to create valid domain objects.
 * These objects can then be modified by setters to change values if needed.
 */
public class ValidDomainObjectFactory {
    public static Car createCar(){
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
        return car;
    }

    public static EmployeeCar createEmployeeCar(){
        EmployeeCar employeeCar = new EmployeeCar();
        employeeCar.setSelectedCar(createCar());
        CarOption a = new CarOption();
        a.setDescription("Trekhaak");
        CarOption b = new CarOption();
        b.setDescription("Parkeersensor");
        employeeCar.setCarOptions(Arrays.asList(a, b));
        employeeCar.setCarStatus(CarStatus.IN_USE);
        employeeCar.setLicensePlate("1-FGH-468");
        employeeCar.setMileage(84_554);
        return employeeCar;
    }

    public static Order createOrder(){
        Order order = new Order();
        order.setAmountPaidByCompany(BigDecimal.valueOf(24000));
        order.setAmountPaidByEmployee(BigDecimal.valueOf(25343.22));
        order.setDateOrdered(LocalDate.of(2012, 5, 5));
        order.setDateReceived(LocalDate.of(2012, 8, 3));
        order.setOrderedCar(createEmployeeCar());
        return order;
    }

    public static RdEmployee createRdEmployee(){
        RdEmployee rdEmployee = new RdEmployee();
        rdEmployee.setInService(true);
        rdEmployee.setFunctionalLevel(4);
        rdEmployee.setFirstName("Fleet");
        rdEmployee.setLastName("Realdolmen");
        rdEmployee.setEmail("fleet.realdolmen@gmail.com");
        rdEmployee.setPassword("password");
        rdEmployee.setCurrentOrder(createOrder());
        return rdEmployee;
    }
}
