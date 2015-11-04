package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.domain.*;
import com.realdolmen.rdfleet.repositories.CarRepository;
import com.realdolmen.rdfleet.repositories.EmployeeCarRepository;
import com.realdolmen.rdfleet.repositories.OrderRepository;
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
    private EmployeeCarRepository employeeCarRepository;

    @Autowired
    public void setEmployeeCarRepository(EmployeeCarRepository employeeCarRepository) {
        this.employeeCarRepository = employeeCarRepository;
    }

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

        if(rdEmployee.getCurrentOrder() != null)
            setEmployeeCarRemoved(rdEmployee);

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

    /**
     * After ordering a car it is set to pending, the fleet employee has to set the car to IN_USE.
     * This will define the car as being used, it will also add the car to the history of the user.
     * The received-date will be set on the day this method is called.
     * @param rdEmployee the employee whos car should be set to in-use
     */
    public void setEmployeeCarInUse(RdEmployee rdEmployee) {
        if(rdEmployee == null || rdEmployee.getId() == null)
            throw new IllegalArgumentException("The employee cannot was not found.");

        Order currentOrder = rdEmployee.getCurrentOrder();

        if(currentOrder == null)
            throw new IllegalArgumentException("The order cannot be null.");

        EmployeeCar employeeCar = currentOrder.getOrderedCar();

        if(employeeCar == null)
            throw new IllegalArgumentException("The car to change status of cannot be null.");

        if(employeeCar.getCarStatus() != CarStatus.PENDING)
            throw new IllegalArgumentException("The car is not in PENDING state.");

        rdEmployee.getOrderHistory().add(rdEmployee.getCurrentOrder());
        rdEmployee.getCurrentOrder().setDateReceived(LocalDate.now());
        employeeCar.setCarStatus(CarStatus.IN_USE);

        rdEmployeeRepository.save(rdEmployee);
    }

    /**
     * The car from an employee can be moved to the free pool so another employee can instead use this car.
     * The current employee will then not have a current car and will have to order a new one.
     * @param rdEmployee the employee of which the car should be moved to the free pool
     */
    public void setEmployeeCarInFreePool(RdEmployee rdEmployee){
        setEmployeeCarToStatus(rdEmployee, CarStatus.NOT_USED);
    }

    /**
     * The car from an employee can be removed from the system.
     * This could happen on a total-loss off the car or when the max mileage/date is reached for his car.
     * The employee will then have to order a new car.
     * @param rdEmployee the employee of which the car should be set to REMOVED status
     */
    public void setEmployeeCarRemoved(RdEmployee rdEmployee){
        setEmployeeCarToStatus(rdEmployee, CarStatus.REMOVED);
    }

    private void setEmployeeCarToStatus(RdEmployee rdEmployee, CarStatus status){
        if(rdEmployee == null || rdEmployee.getId() == null)
            throw new IllegalArgumentException("Existing employee must be provided.");

        if(rdEmployee.getCurrentOrder() == null)
            throw new IllegalArgumentException("The employee does not have an order.");

        if(rdEmployee.getCurrentOrder().getOrderedCar() == null)
            throw new IllegalArgumentException("The employee must have a car.");

        EmployeeCar orderedCar = rdEmployee.getCurrentOrder().getOrderedCar();
        orderedCar.setCarStatus(status);
        employeeCarRepository.save(orderedCar);

        rdEmployee.setCurrentOrder(null);
        rdEmployeeRepository.save(rdEmployee);
    }

    //TODO: test
    public List<Car> findCarsForEmployeeByFunctionalLevel(String email) {
        if (email.isEmpty()) throw new IllegalArgumentException("Email can not be empty");
        RdEmployee rdEmployee = rdEmployeeRepository.findByEmailIgnoreCase(email);
        if (rdEmployee == null) throw new IllegalArgumentException("RdEmployee can not be empty");
        int functionalLevel = rdEmployee.getFunctionalLevel();
        List<Car> cars = new ArrayList<>();
        cars.addAll(carRepository.findByFunctionalLevel(functionalLevel));
        cars.addAll(carRepository.findByFunctionalLevel(functionalLevel - 1));
        cars.addAll(carRepository.findByFunctionalLevel(functionalLevel + 1));
        if (cars.size() == 0) throw new IllegalArgumentException("There are no cars to select from");
        return cars;
    }

    public List<RdEmployee> findAllRdEmployeesInService() {
        return rdEmployeeRepository.findAllEmployeesInService();
    }

    public RdEmployee findRdEmployee(Long id) {
        return rdEmployeeRepository.findOne(id);
    }

    //TODO: test
    public int getFunctionalLevelByEmail(String email) {
        if (email.isEmpty()) throw new IllegalArgumentException("Email can not be empty");
        RdEmployee rdEmployee = rdEmployeeRepository.findByEmailIgnoreCase(email);
        if (rdEmployee == null) throw new IllegalArgumentException("RdEmployee can not be empty");
        return rdEmployee.getFunctionalLevel();
    }

    //TODO: test
    public boolean employeeCanOrderNewCar(String email) {
        return checkIfEmployeeCanOrderCar(email);
    }

    //TODO: test
    public boolean employeeCanOrderNewCar(String email, Long carId) {
        if (carId == null) throw new IllegalArgumentException("Car id can not be null");
        if (carId < 0) throw new IllegalArgumentException("Car id can not be a negative number");
        Car car = carRepository.findOne(carId);
        if (car == null) throw new IllegalArgumentException("Car object can not be null");
        boolean canOrderCar = checkIfEmployeeCanOrderCar(email);
        RdEmployee rdEmployee = rdEmployeeRepository.findByEmailIgnoreCase(email);
        return canOrderCar
                && car.getFunctionalLevel() <= rdEmployee.getFunctionalLevel() + 1
                && car.getFunctionalLevel() >= rdEmployee.getFunctionalLevel() - 1;
    }

    //TODO: test
    private boolean checkIfEmployeeCanOrderCar(String email) {
        if (email.isEmpty()) throw new IllegalArgumentException("Email can not be empty");
        RdEmployee rdEmployee = rdEmployeeRepository.findByEmailIgnoreCase(email);
        if (rdEmployee == null) throw new IllegalArgumentException("RdEmployee can not be empty");
        LocalDate fourYearsAgo = LocalDate.now().minusYears(4);
        if (rdEmployee.getCurrentOrder() == null) return true;
        if (rdEmployee.getCurrentOrder().getDateReceived().isBefore(fourYearsAgo)) return true;
        if (rdEmployee.getCurrentOrder().getOrderedCar().getMileage() > 160000) return true;
        return false;
    }
}
