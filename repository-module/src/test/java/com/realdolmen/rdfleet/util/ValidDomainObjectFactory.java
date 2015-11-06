package com.realdolmen.rdfleet.util;

import com.realdolmen.rdfleet.domain.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        car.setBenefit(BigDecimal.valueOf(158.58));
        car.setPacks(createPacks());
        return car;
    }

    private static List<Pack> createPacks(){
        Pack pack1 = new Pack();
        pack1.setName("Leather Pack");
        pack1.setItems(new ArrayList<>(Arrays.asList("Leather steering wheel", "Leather seats", "Leather dashboard")));
        Pack pack2 = new Pack();
        pack2.setName("LED");
        pack2.setItems(new ArrayList<>(Arrays.asList("LED headlights", "LED dashboard", "LED backlights")));
        return new ArrayList<>(Arrays.asList(pack1, pack2));
    }

    public static CarOption createCarOption(String desc){
        CarOption carOption = new CarOption();
        carOption.setDescription(desc);
        return  carOption;
    }

    public static EmployeeCar createEmployeeCar(){
        EmployeeCar employeeCar = new EmployeeCar();
        employeeCar.setSelectedCar(createCar());
        employeeCar.setCarOptions(Arrays.asList(createCarOption("Trekhaak"), createCarOption("Lederen bekleding")));
        employeeCar.setCarStatus(CarStatus.IN_USE);
        employeeCar.setLicensePlate("1-FGH-468");
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

    public static Pack createPack(){
        Pack pack = new Pack();
        pack.setName("Leather Pack");
        pack.setItems(Arrays.asList("Leather steering wheel", "Leather seating", "Leather dashboard"));
        return pack;
    }
}
