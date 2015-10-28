package com.realdolmen.rdfleet.scheduling;

import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.domain.EmployeeCar;
import com.realdolmen.rdfleet.repositories.CarRepository;
import com.realdolmen.rdfleet.repositories.EmployeeCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by JSTAX29 on 28/10/2015.
 */
@Component
public class ScheduledTasks {

    @Autowired
    private EmployeeCarRepository employeeCarRepository;

    /**
     * Will run the mileage processing every saturday at 2am
     */
    @Scheduled(cron = "0 0 2 * * SAT")
    public void processCarMileage(){
        List<EmployeeCar> all = employeeCarRepository.findAll();

        for(EmployeeCar employeeCar : all){
            System.out.println("One of the found employeeCars is: " + employeeCar.getLicensePlate());
        }
    }
}
