package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.domain.*;
import com.realdolmen.rdfleet.repositories.CarRepository;
import com.realdolmen.rdfleet.repositories.EmployeeCarRepository;
import com.realdolmen.rdfleet.repositories.RdEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
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

        if (rdEmployee.getCurrentOrder() != null)
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
            throw new IllegalArgumentException("The employee to be modified cannot be null.");
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
     *
     * @param rdEmployee the employee whos car should be set to in-use
     */
    public void setEmployeeCarInUse(RdEmployee rdEmployee, String givenLicensePlate) {
        if (rdEmployee == null || rdEmployee.getId() == null)
            throw new IllegalArgumentException("The employee was not found.");

        Order currentOrder = rdEmployee.getCurrentOrder();

        if (currentOrder == null)
            throw new IllegalArgumentException("The order cannot be null.");

        EmployeeCar employeeCar = currentOrder.getOrderedCar();

        if (employeeCar == null)
            throw new IllegalArgumentException("The car to change status of cannot be null.");

        if (employeeCar.getCarStatus() != CarStatus.PENDING)
            throw new IllegalArgumentException("The car is not in PENDING state.");

        String upperCaseLicensePlate = givenLicensePlate.toUpperCase();
        if (!upperCaseLicensePlate.matches("^\\d-[A-Z]{3}-\\d{3}$"))
            throw new IllegalArgumentException("The license plate is not valid. It must have the following pattern: 0-XXX-000");

        EmployeeCar byLicensePlateIgnoreCase = employeeCarRepository.findByLicensePlateIgnoreCase(upperCaseLicensePlate);
        if (byLicensePlateIgnoreCase != null)
            throw new IllegalArgumentException("This license plate already exists in the system. Please provide a new one.");

        rdEmployee.getOrderHistory().add(rdEmployee.getCurrentOrder());
        rdEmployee.getCurrentOrder().setDateReceived(LocalDate.now());
        employeeCar.setCarStatus(CarStatus.IN_USE);
        employeeCar.setLicensePlate(upperCaseLicensePlate);

        rdEmployeeRepository.save(rdEmployee);
    }

    /**
     * The car from an employee can be moved to the free pool so another employee can instead use this car.
     * The current employee will then not have a current car and will have to order a new one.
     *
     * @param rdEmployee the employee of which the car should be moved to the free pool
     */
    public void setEmployeeCarInFreePool(RdEmployee rdEmployee) {
        setEmployeeCarToStatus(rdEmployee, CarStatus.NOT_USED);
    }

    /**
     * The car from an employee can be removed from the system.
     * This could happen on a total-loss off the car or when the max mileage/date is reached for his car.
     * The employee will then have to order a new car.
     *
     * @param rdEmployee the employee of which the car should be set to REMOVED status
     */
    public void setEmployeeCarRemoved(RdEmployee rdEmployee) {
        setEmployeeCarToStatus(rdEmployee, CarStatus.REMOVED);
    }

    /**
     * Helper method to combine duplicate code. It will set the status of the car based on the parameters.
     *
     * @param rdEmployee the employee to save and manipulate
     * @param status     the status to set the car to
     */
    private void setEmployeeCarToStatus(RdEmployee rdEmployee, CarStatus status) {
        if (rdEmployee == null || rdEmployee.getId() == null)
            throw new IllegalArgumentException("Existing employee must be provided.");

        if (rdEmployee.getCurrentOrder() == null)
            throw new IllegalArgumentException("The employee does not have an order.");

        if (rdEmployee.getCurrentOrder().getOrderedCar() == null)
            throw new IllegalArgumentException("The employee must have a car.");

        EmployeeCar orderedCar = rdEmployee.getCurrentOrder().getOrderedCar();
        orderedCar.setCarStatus(status);
        employeeCarRepository.save(orderedCar);

        rdEmployee.setCurrentOrder(null);
        rdEmployeeRepository.save(rdEmployee);
    }

    public RdEmployee findEmployeeByLicensePlateOfCurrentCar(String licensePlate) {
        if (licensePlate == null || licensePlate.isEmpty())
            throw new IllegalArgumentException("The license plate should be provided.");

        return rdEmployeeRepository.findRdEmployeeByCurrentOrder_OrderedCar_LicensePlate(licensePlate);
    }


    /**
     * this will call the assignOrderForEmployee
     * uses findCarByLicensePlate until it returns null to ensure licenseplate is unique
     *
     * @param email of an rdemployee
     * @param order new car to order
     */
    public void createEmployeeCarAndDelegateOrderForEmployee(String email, Order order) {
        RdEmployee rdEmployee = findRdEmployeeByEmail(email);
        if (order == null) throw new IllegalArgumentException("Order can not be null");
        EmployeeCar employeeCar = order.getOrderedCar();
        if (employeeCar == null) throw new IllegalArgumentException("EmployeeCar can not be null");
        if (employeeCar.getSelectedCar() == null)
            throw new IllegalArgumentException("Car for employeeCar can not be null");
        if (order.getAmountPaidByCompany() == null)
            throw new IllegalArgumentException("amountPaidByCompany can not be null");
        if (order.getAmountPaidByEmployee() == null)
            throw new IllegalArgumentException("amountPaidByEmployee can not be null");
        assignOrderToEmployee(rdEmployee, order);
    }

    /**
     * find and return an rdemployee by email if email is not empty and find doesn't return an empty object
     *
     * @param email of an rdemployee
     * @return rdEmployee if it isn't null else error
     */
    public RdEmployee findRdEmployeeByEmail(String email) {
        if (email == null || email.isEmpty()) throw new IllegalArgumentException("Email can not be empty");
        RdEmployee rdEmployee = rdEmployeeRepository.findByEmailIgnoreCase(email);
        if (rdEmployee == null) throw new IllegalArgumentException("RdEmployee can not be empty");
        return rdEmployee;
    }

    /**
     * uses getFunctionalLevelByEmail
     * find cars relevant for an employee by his current functional level + one above and one under his current level
     *
     * @param email of an rdemployee
     * @return list of cars founded (func lvl, -1, +1), if empty error
     */
    public List<Car> findCarsForEmployeeByFunctionalLevel(String email) {
        int functionalLevel = getFunctionalLevelByEmail(email);
        List<Car> cars = carRepository.findAllCarsByFunctionalLevelAndIsOrderable(functionalLevel);
        if (cars.isEmpty()) throw new IllegalArgumentException("There are no cars to select from");
        return cars;
    }

    public List<RdEmployee> findAllRdEmployeesInService() {
        return rdEmployeeRepository.findAllEmployeesInService();
    }

    public RdEmployee findRdEmployee(Long id) {
        return rdEmployeeRepository.findOne(id);
    }

    /**
     * calls findRdEmployeeByEmail and gets functional level for the returned rdemployee
     *
     * @param email of an rdemployee
     * @return functionallevel by given email
     */
    public int getFunctionalLevelByEmail(String email) {
        RdEmployee rdEmployeeByEmail = findRdEmployeeByEmail(email);
        return rdEmployeeByEmail.getFunctionalLevel();
    }


    /**
     * calls checkIfEmployeeCanOrderCar and return the returnvalue of that function
     *
     * @param email of an rdemployee
     * @return returns true or false
     */
    public boolean employeeCanOrderNewCar(String email) {
        return checkIfEmployeeCanOrderCar(email);
    }

    public boolean employeeCanOrderEmployeeCar(String email, Long employeeCarId) {
        if (employeeCarId == null) throw new IllegalArgumentException("EmployeeCar id can not be null");
        if (employeeCarId < 0) throw new IllegalArgumentException("EmployeeCar id can not be a negative number");
        EmployeeCar employeeCar = employeeCarRepository.findOne(employeeCarId);
        if (employeeCar == null) throw new IllegalArgumentException("EmployeeCar object can not be null");
        return checkIfEmployeeCanOrderCar(email)
                && employeeCar.getCarStatus() == CarStatus.NOT_USED;
    }


    public boolean employeeCanOrderNewCar(String email, Long carId) {
        if (carId == null) throw new IllegalArgumentException("Car id can not be null");
        if (carId < 0) throw new IllegalArgumentException("Car id can not be a negative number");
        Car car = carRepository.findOne(carId);
        if (car == null) throw new IllegalArgumentException("Car object can not be null");
        boolean canOrderCar = checkIfEmployeeCanOrderCar(email);
        RdEmployee rdEmployee = findRdEmployeeByEmail(email);
        return canOrderCar
                && car.getFunctionalLevel() <= rdEmployee.getFunctionalLevel() + 1
                && car.getFunctionalLevel() >= rdEmployee.getFunctionalLevel() - 1
                && car.isOrderable();
    }


    private boolean checkIfEmployeeCanOrderCar(String email) {
        RdEmployee rdEmployee = findRdEmployeeByEmail(email);
        LocalDate fourYearsAgo = LocalDate.now().minusYears(4);
        if (rdEmployee.getCurrentOrder() == null) return true;
        if (rdEmployee.getCurrentOrder().getDateReceived() == null)
            if (rdEmployee.getCurrentOrder().getOrderedCar().getCarStatus() == CarStatus.PENDING) return false;

        if (rdEmployee.getCurrentOrder().getDateReceived() != null && rdEmployee.getCurrentOrder().getDateReceived().isBefore(fourYearsAgo))
            return true;
        if (rdEmployee.getCurrentOrder().getOrderedCar() == null) return true;
        if (rdEmployee.getCurrentOrder().getOrderedCar().getMileage() > 160000) return true;
        return false;
    }


    public Order createOrderForEmployee(String email, EmployeeCar employeeCar) {
        RdEmployee rdEmployee = findRdEmployeeByEmail(email);
        if (employeeCar == null) throw new IllegalArgumentException("EmployeeCar can not be empty");
        if (employeeCar.getSelectedCar() == null)
            throw new IllegalArgumentException("EmployeeCar selectedCar can not be empty");
        Order order = new Order();
        if (employeeCar.getSelectedCar().getFunctionalLevel() > rdEmployee.getFunctionalLevel()) {
            order.setAmountPaidByCompany(
                    employeeCar.getSelectedCar().getListPrice());
            order.setAmountPaidByEmployee(
                    employeeCar.getSelectedCar().getAmountUpgrade());
        } else {
            order.setAmountPaidByCompany(
                    employeeCar.getSelectedCar().getListPrice());
            order.setAmountPaidByEmployee(
                    BigDecimal.ZERO);
        }
        order.setOrderedCar(employeeCar);
        return order;
    }
}
