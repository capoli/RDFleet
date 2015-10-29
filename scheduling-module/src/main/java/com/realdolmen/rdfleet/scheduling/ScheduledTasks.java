package com.realdolmen.rdfleet.scheduling;

import com.realdolmen.rdfleet.domain.*;
import com.realdolmen.rdfleet.repositories.CarRepository;
import com.realdolmen.rdfleet.repositories.EmployeeCarRepository;
import com.realdolmen.rdfleet.repositories.OrderRepository;
import com.realdolmen.rdfleet.repositories.RdEmployeeRepository;
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
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RdEmployeeRepository rdEmployeeRepository;

    /**
     * Will run the mileage processing every saturday at 2am.
     * It will add the mileage that was sent through the webservice to the cars available in the database.
     * After adding the mileage to the car, the object that has the mileage will be removed.
     */
    @Scheduled(cron = "0 0 2 * * SAT")
    public void processCarMileage(){
        List<EmployeeCar> all = employeeCarRepository.findAll();

        for(EmployeeCar employeeCar : all){
            System.out.println("One of the found employeeCars is: " + employeeCar.getLicensePlate());
        }
    }

    /**
     * Will run the car renewal notification process every saturday at 2am.
     * When a car has reached 160.000km or when the car has been owned for 4 years, the employee should be notified he should get a new car.
     */
//    @Scheduled(cron = "0 0 2 * * SAT")
    @Scheduled(fixedRate = 1000)
    public void checkForNeededCarRenewals(){
        List<RdEmployee> allEmployeesInService = rdEmployeeRepository.findAllEmployeesInService();

        for(RdEmployee employee : allEmployeesInService){
            Order currentOrder = employee.getCurrentOrder();
            if(currentOrder == null) continue;
            EmployeeCar orderedCar = currentOrder.getOrderedCar();
            if(orderedCar == null) continue;

            //If the car is not being used, skip it.
            if(orderedCar.getCarStatus() != CarStatus.IN_USE) continue;

            //If the car was received longer than 4 years ago, send a notification
            LocalDate fourYearsAgo = LocalDate.now().minusYears(4);
            if(currentOrder.getDateReceived().isBefore(fourYearsAgo)){
                //todo: send an email
                System.out.println(String.format("The car with license plate %s, owned by %s %s has been in use for more than 4 years.", orderedCar.getLicensePlate(), employee.getFirstName(), employee.getLastName()));
                //Continue, we sent a message.
                continue;
            }

            //If the car has over 160.000km, also send a notification
            if(orderedCar.getMileage() >= 160_000){
                //todo: send an email
                System.out.println(String.format("The car with license plate %s, owned by %s %s has driven more than 160.000km (%dkm)", orderedCar.getLicensePlate(), employee.getFirstName(), employee.getLastName(), orderedCar.getMileage()));
            }
        }
    }
}
