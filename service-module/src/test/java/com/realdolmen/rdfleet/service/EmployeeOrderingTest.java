package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.domain.*;
import com.realdolmen.rdfleet.repositories.CarRepository;
import com.realdolmen.rdfleet.repositories.EmployeeCarRepository;
import com.realdolmen.rdfleet.repositories.RdEmployeeRepository;
import com.realdolmen.rdfleet.service.EmployeeService;
import com.realdolmen.rdfleet.service.util.ValidDomainObjectFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeOrderingTest {
    private EmployeeCarRepository employeeCarRepositoryMock;
    private RdEmployeeRepository rdEmployeeRepositoryMock;
    private CarRepository carRepositoryMock;

    private EmployeeService employeeService;

    private RdEmployee dbRdEmployee;
    private String dbRdEmployeeEmail;

    @Before
    public void initialize() {
        rdEmployeeRepositoryMock = mock(RdEmployeeRepository.class);
        employeeCarRepositoryMock = mock(EmployeeCarRepository.class);
        carRepositoryMock = mock(CarRepository.class);

        employeeService = new EmployeeService();
        employeeService.setRdEmployeeRepository(rdEmployeeRepositoryMock);
        employeeService.setEmployeeCarRepository(employeeCarRepositoryMock);
        employeeService.setCarRepository(carRepositoryMock);

        dbRdEmployee = ValidDomainObjectFactory.createRdEmployee();
        dbRdEmployee.setId(1l);


        dbRdEmployeeEmail = dbRdEmployee.getEmail();
        when(rdEmployeeRepositoryMock.findByEmailIgnoreCase(dbRdEmployeeEmail)).thenReturn(dbRdEmployee);
    }

    //Test assigning orders to employees
    @Test(expected = IllegalArgumentException.class)
    public void testAssignOrderEmployeeIsNull() {
        employeeService.assignOrderToEmployee(null, dbRdEmployee.getCurrentOrder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssignOrderEmployeeNotFromDb() {
        employeeService.assignOrderToEmployee(ValidDomainObjectFactory.createRdEmployee(), dbRdEmployee.getCurrentOrder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssignOrderOrderIsNull() {
        employeeService.assignOrderToEmployee(dbRdEmployee, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssignOrderOrderedCarIsNull() {
        Order order = ValidDomainObjectFactory.createOrder();
        order.setOrderedCar(null);

        employeeService.assignOrderToEmployee(dbRdEmployee, order);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssignOrderFunctionalLevelCarTooHighFunctionalLevel() {
        dbRdEmployee.getCurrentOrder().getOrderedCar().getSelectedCar().setFunctionalLevel(2);

        employeeService.assignOrderToEmployee(dbRdEmployee, dbRdEmployee.getCurrentOrder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssignOrderFunctionalLevelCarTooLowFunctionalLevel() {
        dbRdEmployee.getCurrentOrder().getOrderedCar().getSelectedCar().setFunctionalLevel(6);

        employeeService.assignOrderToEmployee(dbRdEmployee, dbRdEmployee.getCurrentOrder());
    }

    @Test
    public void testAssignOrderFunctionalLevelCarTooHighButCarNotUsed() {
        dbRdEmployee.getCurrentOrder().getOrderedCar().setCarStatus(CarStatus.NOT_USED);
        dbRdEmployee.getCurrentOrder().getOrderedCar().getSelectedCar().setFunctionalLevel(6);

        employeeService.assignOrderToEmployee(dbRdEmployee, dbRdEmployee.getCurrentOrder());
    }

    @Test
    public void testAssignOrderEverythingOk() {
        employeeService.assignOrderToEmployee(dbRdEmployee, dbRdEmployee.getCurrentOrder());

        verify(rdEmployeeRepositoryMock, times(2)).save(dbRdEmployee);
    }

    @Test
    public void testAssignOrderAndOrderNullEverythingOk() {
        Order currentOrder = dbRdEmployee.getCurrentOrder();
        dbRdEmployee.setCurrentOrder(null);
        employeeService.assignOrderToEmployee(dbRdEmployee, currentOrder);

        verify(rdEmployeeRepositoryMock, times(1)).save(dbRdEmployee);
    }

    @Test
    public void testAssignOrderSetsCarStatusPending() {
        dbRdEmployee.getCurrentOrder().setDateReceived(null);
        employeeService.assignOrderToEmployee(dbRdEmployee, dbRdEmployee.getCurrentOrder());

        assertEquals(CarStatus.PENDING, dbRdEmployee.getCurrentOrder().getOrderedCar().getCarStatus());
    }

    @Test
    public void testAssignOrderSetsDateOrderedToday() {
        employeeService.assignOrderToEmployee(dbRdEmployee, dbRdEmployee.getCurrentOrder());

        assertEquals(LocalDate.now(), dbRdEmployee.getCurrentOrder().getDateOrdered());
    }

    @Test
    public void testAssignOrderOrderIsSetOnEmployee() {
        Order order = ValidDomainObjectFactory.createOrder();

        employeeService.assignOrderToEmployee(dbRdEmployee, order);

        assertEquals(order, dbRdEmployee.getCurrentOrder());
    }

    @Test
    public void testAssignOrderOldCarIsSetToRemoved(){
        EmployeeCar oldCar = dbRdEmployee.getCurrentOrder().getOrderedCar();
        assertEquals(CarStatus.IN_USE, oldCar.getCarStatus());

        Order newOrder = ValidDomainObjectFactory.createOrder();
        employeeService.assignOrderToEmployee(dbRdEmployee, newOrder);

        assertEquals(CarStatus.REMOVED, oldCar.getCarStatus());
        assertEquals(CarStatus.PENDING, dbRdEmployee.getCurrentOrder().getOrderedCar().getCarStatus());
    }

    //Test finding employees by email
    @Test
    public void testFindRdEmployeeByEmailFindsEmployee(){
        when(rdEmployeeRepositoryMock.findByEmailIgnoreCase(dbRdEmployee.getEmail())).thenReturn(dbRdEmployee);

        assertEquals(dbRdEmployee, employeeService.findRdEmployeeByEmail(dbRdEmployee.getEmail()));

        verify(rdEmployeeRepositoryMock).findByEmailIgnoreCase(dbRdEmployee.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindRdEmployeeByEmailEmailIsEmpty(){
        employeeService.findRdEmployeeByEmail("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindRdEmployeeByEmailEmailIsNull(){
        employeeService.findRdEmployeeByEmail(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindRdEmployeeByEmailNoEmployeeFound(){
        employeeService.findRdEmployeeByEmail("Other@Mail.com");
    }

    //Tests createEmployeeCarAndDelegateOrderForEmployee
    @Test
    public void testCreateAndDelegateOrderAllOk(){
        Order newOrder = dbRdEmployee.getCurrentOrder();
        dbRdEmployee.setCurrentOrder(null);

        assertNull(dbRdEmployee.getCurrentOrder());
        employeeService.createEmployeeCarAndDelegateOrderForEmployee(dbRdEmployeeEmail, newOrder);
        assertEquals(newOrder, dbRdEmployee.getCurrentOrder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAndDelegateOrderIsNull(){
        employeeService.createEmployeeCarAndDelegateOrderForEmployee(dbRdEmployeeEmail, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAndDelegateOrderCarIsNull(){
        Order newOrder = dbRdEmployee.getCurrentOrder();
        newOrder.setOrderedCar(null);

        String dbRdEmployeeEmail = dbRdEmployee.getEmail();
        employeeService.createEmployeeCarAndDelegateOrderForEmployee(dbRdEmployeeEmail, newOrder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAndDelegateOrderCarModelIsNull(){
        EmployeeCar orderedCar = dbRdEmployee.getCurrentOrder().getOrderedCar();
        orderedCar.setSelectedCar(null);

        String dbRdEmployeeEmail = dbRdEmployee.getEmail();
        employeeService.createEmployeeCarAndDelegateOrderForEmployee(dbRdEmployeeEmail, dbRdEmployee.getCurrentOrder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAndDelegateOrderAmountByCompanyIsNull(){
        Order newOrder = dbRdEmployee.getCurrentOrder();
        newOrder.setAmountPaidByCompany(null);

        String dbRdEmployeeEmail = dbRdEmployee.getEmail();
        employeeService.createEmployeeCarAndDelegateOrderForEmployee(dbRdEmployeeEmail, newOrder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateAndDelegateOrderAmountByEmployeeIsNull(){
        Order newOrder = dbRdEmployee.getCurrentOrder();
        newOrder.setAmountPaidByEmployee(null);

        String dbRdEmployeeEmail = dbRdEmployee.getEmail();
        employeeService.createEmployeeCarAndDelegateOrderForEmployee(dbRdEmployeeEmail, newOrder);
    }

    //Tests findCarsForEmployeeByFunctionalLevel
    @Test
    public void testFindCarsForEmployeeByFuncLevelReturnsCorrectCars(){
        int funcLvl = dbRdEmployee.getFunctionalLevel();
        List<Car> possibleCars = new ArrayList<>(Arrays.asList(ValidDomainObjectFactory.createCar(), ValidDomainObjectFactory.createCar()));
        when(carRepositoryMock.findAllCarsByFunctionalLevelAndIsOrderable(funcLvl)).thenReturn(possibleCars);

        assertEquals(possibleCars, employeeService.findCarsForEmployeeByFunctionalLevel(dbRdEmployeeEmail));
        verify(carRepositoryMock).findAllCarsByFunctionalLevelAndIsOrderable(funcLvl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindCarsForEmployeeByFuncLevelNoCarsFound(){
        int funcLvl = 2;
        List<Car> possibleCars = new ArrayList<>(Arrays.asList(ValidDomainObjectFactory.createCar(), ValidDomainObjectFactory.createCar()));
        when(carRepositoryMock.findAllCarsByFunctionalLevelAndIsOrderable(funcLvl)).thenReturn(possibleCars);

        assertEquals(possibleCars, employeeService.findCarsForEmployeeByFunctionalLevel(dbRdEmployeeEmail));
        verify(carRepositoryMock).findAllCarsByFunctionalLevelAndIsOrderable(funcLvl);
    }

    //Tests getFunctionalLevelByEmail
    @Test
    public void testGetFunctionalLevelByEmailReturnsFunctionalLevel(){
        int funcLvl = 5;
        dbRdEmployee.setFunctionalLevel(funcLvl);

        when(rdEmployeeRepositoryMock.findByEmailIgnoreCase(dbRdEmployeeEmail)).thenReturn(dbRdEmployee);

        assertEquals(funcLvl, employeeService.getFunctionalLevelByEmail(dbRdEmployeeEmail));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFunctionalLevelByEmailNoEmployeeFound(){
        employeeService.getFunctionalLevelByEmail("None@Found.sadface");
    }

    //Tests employeeCanOrderNewCar
    @Test(expected = IllegalArgumentException.class)
    public void testEmployeeCanOrderNewCarNoEmployeeFound(){
        employeeService.employeeCanOrderNewCar("not@found.com");
    }


    @Test
    public void testEmployeeCanOrderNewCarEmployeeHasNoOrder(){
        dbRdEmployee.setCurrentOrder(null);

        assertTrue(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail));
    }


    @Test
    public void testEmployeeCanOrderNewCarEmployeeHasOrderWithoutDateReceivedButCarStatusIsPending(){
        dbRdEmployee.getCurrentOrder().setDateReceived(null);
        dbRdEmployee.getCurrentOrder().getOrderedCar().setCarStatus(CarStatus.PENDING);

        assertFalse(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail));
    }

    @Test
    public void testEmployeeCanOrderNewCarEmployeeHasOrderWithoutDateReceivedButCarStatusIsNotPending(){
        dbRdEmployee.getCurrentOrder().setDateReceived(null);

        assertFalse(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail));
    }

    @Test
    public void testEmployeeCanOrderNewCarEmployeeHasOrderButWasReceivedFourYearsAgo(){
        dbRdEmployee.getCurrentOrder().setDateReceived(LocalDate.now().minusYears(5));
        assertTrue(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail));
    }

    @Test
    public void testEmployeeCanOrderNewCarOrderHasNoCarSpecified(){
        dbRdEmployee.getCurrentOrder().setOrderedCar(null);
        assertTrue(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail));
    }

    @Test
    public void testEmployeeCanOrderNewCarAlreadyHasAnOrder(){
        assertFalse(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail));
    }

    @Test
    public void testEmployeeCanOrderNewCarCarHasOver160kMileage(){
        dbRdEmployee.getCurrentOrder().getOrderedCar().setMileage(165_546);
        assertTrue(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail));
    }

    @Test
    public void testEmployeeCanOrderNewCarCarHasLessThan160kMileage(){
        dbRdEmployee.getCurrentOrder().getOrderedCar().setMileage(16_546);
        assertFalse(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail));
    }

    //Tests employeeCanOrderEmployeeCar
    @Test
    public void testEmployeeCanOrderEmployeeCarAllOk(){
        dbRdEmployee.setCurrentOrder(null);
        EmployeeCar employeeCar = ValidDomainObjectFactory.createEmployeeCar();
        employeeCar.setCarStatus(CarStatus.NOT_USED);
        when(employeeCarRepositoryMock.findOne(1l)).thenReturn(employeeCar);

        assertTrue(employeeService.employeeCanOrderEmployeeCar(dbRdEmployeeEmail, 1l));
        verify(employeeCarRepositoryMock).findOne(1l);
    }

    @Test
    public void testEmployeeCanOrderEmployeeCarAlreadyHasOrder(){
        EmployeeCar employeeCar = ValidDomainObjectFactory.createEmployeeCar();
        employeeCar.setCarStatus(CarStatus.NOT_USED);
        when(employeeCarRepositoryMock.findOne(1l)).thenReturn(employeeCar);

        assertFalse(employeeService.employeeCanOrderEmployeeCar(dbRdEmployeeEmail, 1l));
        verify(employeeCarRepositoryMock).findOne(1l);
    }

    @Test
    public void testEmployeeCanOrderEmployeeCarCarStatusInUse(){
        dbRdEmployee.setCurrentOrder(null);
        EmployeeCar employeeCar = ValidDomainObjectFactory.createEmployeeCar();
        employeeCar.setCarStatus(CarStatus.IN_USE);
        when(employeeCarRepositoryMock.findOne(1l)).thenReturn(employeeCar);

        assertFalse(employeeService.employeeCanOrderEmployeeCar(dbRdEmployeeEmail, 1l));
        verify(employeeCarRepositoryMock).findOne(1l);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmployeeCanOrderEmployeeCarNoIdGiven(){
        dbRdEmployee.setCurrentOrder(null);
        employeeService.employeeCanOrderEmployeeCar(dbRdEmployeeEmail, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmployeeCanOrderEmployeeCarIdIsNegative(){
        dbRdEmployee.setCurrentOrder(null);
        employeeService.employeeCanOrderEmployeeCar(dbRdEmployeeEmail, -5l);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmployeeCanOrderEmployeeCarNotFound(){
        dbRdEmployee.setCurrentOrder(null);
        employeeService.employeeCanOrderEmployeeCar(dbRdEmployeeEmail, 2l);
    }

    //Tests employeeCanOrderNewCar
    @Test
    public void testEmployeeCanOrderNewCarAllOk(){
        Car car = ValidDomainObjectFactory.createCar();
        car.setFunctionalLevel(5);
        dbRdEmployee.setFunctionalLevel(5);
        dbRdEmployee.setCurrentOrder(null);

        when(carRepositoryMock.findOne(1l)).thenReturn(car);

        assertTrue(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail, 1l));
        verify(carRepositoryMock).findOne(1l);
    }

    @Test
    public void testEmployeeCanOrderNewCarAlreadyHasOrder(){
        Car car = ValidDomainObjectFactory.createCar();
        when(carRepositoryMock.findOne(1l)).thenReturn(car);

        assertFalse(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail, 1l));
        verify(carRepositoryMock).findOne(1l);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmployeeCanOrderNewCarNoIdGiven(){
        dbRdEmployee.setCurrentOrder(null);
        employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmployeeCanOrderNewCarIdIsNegative(){
        dbRdEmployee.setCurrentOrder(null);
        employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail, -5l);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmployeeCanOrderNewCarNotFound(){
        dbRdEmployee.setCurrentOrder(null);
        employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail, 2l);
    }

    @Test
    public void testEmployeeCanOrderNewCarFunctionalLevelOfEmployeeTooLow(){
        Car car = ValidDomainObjectFactory.createCar();
        car.setFunctionalLevel(5);
        dbRdEmployee.setFunctionalLevel(2);
        dbRdEmployee.setCurrentOrder(null);

        when(carRepositoryMock.findOne(1l)).thenReturn(car);

        assertFalse(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail, 1l));
        verify(carRepositoryMock).findOne(1l);        
        
    }

    @Test
    public void testEmployeeCanOrderNewCarFunctionalLevelOfEmployeeTooHigh(){
        Car car = ValidDomainObjectFactory.createCar();
        car.setFunctionalLevel(2);
        dbRdEmployee.setFunctionalLevel(5);
        dbRdEmployee.setCurrentOrder(null);

        when(carRepositoryMock.findOne(1l)).thenReturn(car);

        assertFalse(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail, 1l));
        verify(carRepositoryMock).findOne(1l);
    }

    @Test
    public void testEmployeeCanOrderNewCarFunctionalLevelOfEmployeeOneHigher(){
        Car car = ValidDomainObjectFactory.createCar();
        car.setFunctionalLevel(2);
        dbRdEmployee.setFunctionalLevel(3);
        dbRdEmployee.setCurrentOrder(null);

        when(carRepositoryMock.findOne(1l)).thenReturn(car);

        assertTrue(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail, 1l));
        verify(carRepositoryMock).findOne(1l);
    }

    @Test
    public void testEmployeeCanOrderNewCarFunctionalLevelOfEmployeeOneLower(){
        Car car = ValidDomainObjectFactory.createCar();
        car.setFunctionalLevel(4);
        dbRdEmployee.setFunctionalLevel(3);
        dbRdEmployee.setCurrentOrder(null);

        when(carRepositoryMock.findOne(1l)).thenReturn(car);

        assertTrue(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail, 1l));
        verify(carRepositoryMock).findOne(1l);
    }


    @Test
    public void testEmployeeCanOrderNewCarFunctionalLevelOfEmployeeEqual(){
        Car car = ValidDomainObjectFactory.createCar();
        car.setFunctionalLevel(3);
        dbRdEmployee.setFunctionalLevel(3);
        dbRdEmployee.setCurrentOrder(null);


        when(carRepositoryMock.findOne(1l)).thenReturn(car);

        assertTrue(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail, 1l));
        verify(carRepositoryMock).findOne(1l);
    }
    
    @Test
    public void testEmployeeCanOrderNewCarCarIsNotOrderable(){
        Car car = ValidDomainObjectFactory.createCar();
        car.setFunctionalLevel(5);
        car.setOrderable(false);
        dbRdEmployee.setFunctionalLevel(5);
        dbRdEmployee.setCurrentOrder(null);


        when(carRepositoryMock.findOne(1l)).thenReturn(car);

        assertFalse(employeeService.employeeCanOrderNewCar(dbRdEmployeeEmail, 1l));
        verify(carRepositoryMock).findOne(1l);
    }


    //Tests createOrderForEmployee
    @Test(expected = IllegalArgumentException.class)
    public void testCreateOrderForEmployeeNoEmployeeCarGiven(){
        employeeService.createOrderForEmployee(dbRdEmployeeEmail, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOrderForEmployeeNoCarSpecifiedWithEmployeeCar(){
        dbRdEmployee.setCurrentOrder(null);
        EmployeeCar employeeCar = ValidDomainObjectFactory.createEmployeeCar();
        employeeCar.setSelectedCar(null);

        employeeService.createOrderForEmployee(dbRdEmployeeEmail, employeeCar);
    }

    @Test
    public void testCreateOrderForEmployeeCarHigherFunctionalLevelSetsListPriceForCompany(){
        dbRdEmployee.setCurrentOrder(null);
        EmployeeCar employeeCar = ValidDomainObjectFactory.createEmployeeCar();
        employeeCar.getSelectedCar().setFunctionalLevel(5);
        dbRdEmployee.setFunctionalLevel(4);

        Order orderForEmployee = employeeService.createOrderForEmployee(dbRdEmployeeEmail, employeeCar);


        assertEquals(employeeCar.getSelectedCar().getListPrice(), orderForEmployee.getAmountPaidByCompany());
    }

    @Test
    public void testCreateOrderForEmployeeCarHigherCarFunctionalLevelSetsAmountUpgradeForEmployee(){
        dbRdEmployee.setCurrentOrder(null);
        EmployeeCar employeeCar = ValidDomainObjectFactory.createEmployeeCar();
        employeeCar.getSelectedCar().setFunctionalLevel(5);
        dbRdEmployee.setFunctionalLevel(4);

        Order orderForEmployee = employeeService.createOrderForEmployee(dbRdEmployeeEmail, employeeCar);

        assertEquals(employeeCar.getSelectedCar().getAmountUpgrade(), orderForEmployee.getAmountPaidByEmployee());
    }

    @Test
    public void testCreateOrderForEmployeeLowerCarFunctionalLevelSetsListPriceForCompany(){
        dbRdEmployee.setCurrentOrder(null);
        EmployeeCar employeeCar = ValidDomainObjectFactory.createEmployeeCar();
        employeeCar.getSelectedCar().setFunctionalLevel(4);
        dbRdEmployee.setFunctionalLevel(5);

        Order orderForEmployee = employeeService.createOrderForEmployee(dbRdEmployeeEmail, employeeCar);

        assertEquals(employeeCar.getSelectedCar().getListPrice(), orderForEmployee.getAmountPaidByCompany());
    }

    @Test
    public void testCreateOrderForEmployeeLowerCarFunctionalLevelSetsAmountForEmployeeZero(){
        dbRdEmployee.setCurrentOrder(null);
        EmployeeCar employeeCar = ValidDomainObjectFactory.createEmployeeCar();
        employeeCar.getSelectedCar().setFunctionalLevel(4);
        dbRdEmployee.setFunctionalLevel(5);

        Order orderForEmployee = employeeService.createOrderForEmployee(dbRdEmployeeEmail, employeeCar);

        assertEquals(BigDecimal.ZERO, orderForEmployee.getAmountPaidByEmployee());
    }

    @Test
    public void testCreateOrderForEmployeeGivesOrderWithSpecifiedCar(){
        dbRdEmployee.setCurrentOrder(null);
        EmployeeCar employeeCar = ValidDomainObjectFactory.createEmployeeCar();

        Order orderForEmployee = employeeService.createOrderForEmployee(dbRdEmployeeEmail, employeeCar);

        assertEquals(employeeCar, orderForEmployee.getOrderedCar());
    }
}
