package com.realdolmen.rdfleet;

import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.domain.CarStatus;
import com.realdolmen.rdfleet.domain.EmployeeCar;
import com.realdolmen.rdfleet.domain.CarOption;
import com.realdolmen.rdfleet.repositories.EmployeeCarRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

/**
 * Created by JSTAX29 on 28/10/2015.
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Main.class).profiles("production").run();
        Car car = new Car();
        car.setFunctionalLevel(2);
        car.setMake("Audi");
        car.setModel("A1");

        EmployeeCar employeeCar = new EmployeeCar();
        employeeCar.setSelectedCar(car);
        CarOption a = new CarOption();
        a.setDescription("Trekhaak");
        CarOption b = new CarOption();
        b.setDescription("Parkeersensor");
        employeeCar.setCarOptions(Arrays.asList(a, b));
        employeeCar.setCarStatus(CarStatus.NOT_USED);

        EmployeeCarRepository employeeCarRepository = context.getBean(EmployeeCarRepository.class);
        employeeCarRepository.save(employeeCar);
    }
}
