package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.domain.CarStatus;
import com.realdolmen.rdfleet.domain.Order;
import com.realdolmen.rdfleet.domain.RdEmployee;
import com.realdolmen.rdfleet.repositories.RdEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Created by JSTAX29 on 1/11/2015.
 */
@Service
public class EmployeeService {
    private RdEmployeeRepository rdEmployeeRepository;

    @Autowired
    public void setRdEmployeeRepository(RdEmployeeRepository rdEmployeeRepository) {
        this.rdEmployeeRepository = rdEmployeeRepository;
    }

    public void assignOrderToEmployee(RdEmployee rdEmployee, Order order){
        if(rdEmployee == null)
            throw new IllegalArgumentException("The employee to assign and order to cannot be null.");
        if(rdEmployee.getId() == null)
            throw new IllegalArgumentException(String.format("The id of employee %s %s is null so it did not originate out of the database.", rdEmployee.getFirstName(), rdEmployee.getLastName()));
        if(order == null)
            throw new IllegalArgumentException("The order to be assigned to an employee cannot be null.");
        if(order.getOrderedCar() == null)
            throw new IllegalArgumentException("The order should have a car assigned to it.");

        order.setDateOrdered(LocalDate.now());
        rdEmployee.setCurrentOrder(order);
        rdEmployeeRepository.save(rdEmployee);
    }

    public void removeEmployeeFromCompany(RdEmployee rdEmployee){
        if(rdEmployee == null)
            throw new IllegalArgumentException("The employee to be removed cannot be null.");
        if(rdEmployee.getId() == null)
            throw new IllegalArgumentException(String.format("The id of employee %s %s is null so it did not originate out of the database.", rdEmployee.getFirstName(), rdEmployee.getLastName()));

        if(rdEmployee.getCurrentOrder() != null && rdEmployee.getCurrentOrder().getOrderedCar() != null)
            rdEmployee.getCurrentOrder().getOrderedCar().setCarStatus(CarStatus.NOT_USED);

        rdEmployee.setInService(false);
        rdEmployeeRepository.save(rdEmployee);
    }

    public void decrementEmployeeFunctionalLevel(RdEmployee rdEmployee){
        modifyFunctionalLevel(rdEmployee, false);
    }

    public void incrementEmployeeFunctionalLevel(RdEmployee rdEmployee){
        modifyFunctionalLevel(rdEmployee, true);
    }

    private void modifyFunctionalLevel(RdEmployee rdEmployee, boolean increment){
        if(rdEmployee == null)
            throw new IllegalArgumentException("The employee to be removed cannot be null.");
        if(rdEmployee.getId() == null)
            throw new IllegalArgumentException(String.format("The id of employee %s %s is null so it did not originate out of the database.", rdEmployee.getFirstName(), rdEmployee.getLastName()));

        if(!increment){
            if(rdEmployee.getFunctionalLevel() <= 2)
                throw new IllegalArgumentException(String.format("The employee %s %s already has a functional level of %d, it cannot be lowered.", rdEmployee.getFirstName(), rdEmployee.getLastName(), rdEmployee.getFunctionalLevel()));

            rdEmployee.setFunctionalLevel(rdEmployee.getFunctionalLevel() - 1);
        }else{
            if(rdEmployee.getFunctionalLevel() >= 7)
                throw new IllegalArgumentException(String.format("The employee %s %s already has a functional level of %d, it cannot be incremented.",  rdEmployee.getFirstName(), rdEmployee.getLastName(),rdEmployee.getFunctionalLevel()));

            rdEmployee.setFunctionalLevel(rdEmployee.getFunctionalLevel() + 1);
        }

        rdEmployeeRepository.save(rdEmployee);
    }
}
