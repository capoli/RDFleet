package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.domain.CarStatus;
import com.realdolmen.rdfleet.domain.Order;
import com.realdolmen.rdfleet.domain.RdEmployee;
import com.realdolmen.rdfleet.repositories.CarRepository;
import com.realdolmen.rdfleet.repositories.RdEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JSTAX29 on 1/11/2015.
 */
@Service
public class EmployeeService {
    private RdEmployeeRepository rdEmployeeRepository;
    private CarRepository carRepository;

    @Autowired
    public void setRdEmployeeRepository(RdEmployeeRepository rdEmployeeRepository) {
        this.rdEmployeeRepository = rdEmployeeRepository;
    }

    @Autowired
    public void setCarRepository(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Assigns an order of a car to an employee. The RdEmployee and the order cannot be null. The car of the order also cannot be null.
     *
     * @param rdEmployee the employee found from the database
     * @param order      the order that should be linked to the employee
     */
    public void assignOrderToEmployee(RdEmployee rdEmployee, Order order) {
        if (rdEmployee == null)
            throw new IllegalArgumentException("The employee to assign and order to cannot be null.");
        if (rdEmployee.getId() == null)
            throw new IllegalArgumentException(String.format("The id of employee %s %s is null so it did not originate out of the database.", rdEmployee.getFirstName(), rdEmployee.getLastName()));
        if (order == null)
            throw new IllegalArgumentException("The order to be assigned to an employee cannot be null.");
        if (order.getOrderedCar() == null)
            throw new IllegalArgumentException("The order should have a car assigned to it.");

        if ((order.getOrderedCar().getSelectedCar().getFunctionalLevel() > rdEmployee.getFunctionalLevel() + 1 ||
                order.getOrderedCar().getSelectedCar().getFunctionalLevel() < rdEmployee.getFunctionalLevel() - 1) &&
                order.getOrderedCar().getCarStatus() != CarStatus.NOT_USED)
            throw new IllegalArgumentException("The ordered car differs more than 1 functional level from the employee's functional level.");

        order.getOrderedCar().setCarStatus(CarStatus.PENDING);
        order.setDateOrdered(LocalDate.now());
        rdEmployee.setCurrentOrder(order);
        rdEmployeeRepository.save(rdEmployee);
    }

    /**
     * "Removes" the employee from the company. It actually just changes his inService status to false, sets his current car to NOT_USED and sets his current order to null.
     * This allows the car to be re-used in the free-pool.
     *
     * @param rdEmployee the rdEmployee to be removed from the company.
     */
    public void removeEmployeeFromCompany(RdEmployee rdEmployee) {
        if (rdEmployee == null)
            throw new IllegalArgumentException("The employee to be removed cannot be null.");
        if (rdEmployee.getId() == null)
            throw new IllegalArgumentException(String.format("The id of employee %s %s is null so it did not originate out of the database.", rdEmployee.getFirstName(), rdEmployee.getLastName()));

        if (rdEmployee.getCurrentOrder() != null && rdEmployee.getCurrentOrder().getOrderedCar() != null)
            rdEmployee.getCurrentOrder().getOrderedCar().setCarStatus(CarStatus.NOT_USED);

        rdEmployee.setCurrentOrder(null);
        rdEmployee.setInService(false);
        rdEmployeeRepository.save(rdEmployee);
    }

    /**
     * Decrements the functional level of the employee.
     *
     * @param rdEmployee the employee who's functional level should be decremented.
     */
    public void decrementEmployeeFunctionalLevel(RdEmployee rdEmployee) {
        modifyFunctionalLevel(rdEmployee, false);
    }

    /**
     * Increments the functional level of the employee.
     *
     * @param rdEmployee the employee who's functional level should be incremented.
     */
    public void incrementEmployeeFunctionalLevel(RdEmployee rdEmployee) {
        modifyFunctionalLevel(rdEmployee, true);
    }

    /**
     * Helper method to re-use code for incrementing and decrementing functional levels.
     *
     * @param rdEmployee the employee who's functional level should be incremented or decremented.
     * @param increment  true if his functional level should be incremented, false if it should be decremented.
     */
    private void modifyFunctionalLevel(RdEmployee rdEmployee, boolean increment) {
        if (rdEmployee == null)
            throw new IllegalArgumentException("The employee to be removed cannot be null.");
        if (rdEmployee.getId() == null)
            throw new IllegalArgumentException(String.format("The id of employee %s %s is null so it did not originate out of the database.", rdEmployee.getFirstName(), rdEmployee.getLastName()));

        if (!increment) {
            if (rdEmployee.getFunctionalLevel() <= 2)
                throw new IllegalArgumentException(String.format("The employee %s %s already has a functional level of %d, it cannot be lowered.", rdEmployee.getFirstName(), rdEmployee.getLastName(), rdEmployee.getFunctionalLevel()));

            rdEmployee.setFunctionalLevel(rdEmployee.getFunctionalLevel() - 1);
        } else {
            if (rdEmployee.getFunctionalLevel() >= 7)
                throw new IllegalArgumentException(String.format("The employee %s %s already has a functional level of %d, it cannot be incremented.", rdEmployee.getFirstName(), rdEmployee.getLastName(), rdEmployee.getFunctionalLevel()));

            rdEmployee.setFunctionalLevel(rdEmployee.getFunctionalLevel() + 1);
        }

        rdEmployeeRepository.save(rdEmployee);
    }

    //TODO: test
    public List<Car> findCarsForEmployeeByFunctionalLevel(String email) {
        if(email.isEmpty()) throw new IllegalArgumentException("email can not be empty");
        int functionalLevel = rdEmployeeRepository.findByEmailIgnoreCase(email).getFunctionalLevel();
        List<Car> cars = new ArrayList<>();
        cars.addAll(carRepository.findByFunctionalLevel(functionalLevel));
        cars.addAll(carRepository.findByFunctionalLevel(functionalLevel - 1));
        cars.addAll(carRepository.findByFunctionalLevel(functionalLevel + 1));
        return cars;
    }
	
    public List<RdEmployee> findAllRdEmployeesInService(){
        return rdEmployeeRepository.findAllEmployeesInService();
    }

    public RdEmployee findRdEmployee(Long id){
        return rdEmployeeRepository.findOne(id);
    }
}
