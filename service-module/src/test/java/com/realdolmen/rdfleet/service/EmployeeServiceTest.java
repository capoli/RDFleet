package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.domain.CarStatus;
import com.realdolmen.rdfleet.domain.EmployeeCar;
import com.realdolmen.rdfleet.domain.Order;
import com.realdolmen.rdfleet.domain.RdEmployee;
import com.realdolmen.rdfleet.repositories.EmployeeCarRepository;
import com.realdolmen.rdfleet.repositories.RdEmployeeRepository;
import com.realdolmen.rdfleet.service.util.ValidDomainObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {
    private EmployeeCarRepository employeeCarRepositoryMock;
    private RdEmployeeRepository rdEmployeeRepositoryMock;
    private EmployeeService employeeService;

    private RdEmployee dbRdEmployee;

    @Before
    public void initialize() {
        rdEmployeeRepositoryMock = mock(RdEmployeeRepository.class);
        employeeCarRepositoryMock = mock(EmployeeCarRepository.class);

        employeeService = new EmployeeService();
        employeeService.setRdEmployeeRepository(rdEmployeeRepositoryMock);
        employeeService.setEmployeeCarRepository(employeeCarRepositoryMock);

        dbRdEmployee = ValidDomainObjectFactory.createRdEmployee();
        dbRdEmployee.setId(1000l);

    }


    //Removing employees tests
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEmployeeEmployeeIsNull() {
        employeeService.removeEmployeeFromCompany(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveEmployeeEmployeeIsNotFromDb() {
        employeeService.removeEmployeeFromCompany(ValidDomainObjectFactory.createRdEmployee());
    }

    @Test
    public void testRemoveEmployeeCarStatusIsSetOnNotUsed() {
        Order order = ValidDomainObjectFactory.createOrder();
        dbRdEmployee.setCurrentOrder(order);
        employeeService.removeEmployeeFromCompany(dbRdEmployee);

        assertEquals(CarStatus.NOT_USED, order.getOrderedCar().getCarStatus());
    }

    @Test
    public void testRemoveEmployeeCarIsNullButStillRemovesEmployee() {
        dbRdEmployee.setCurrentOrder(null);

        employeeService.removeEmployeeFromCompany(dbRdEmployee);

        verify(rdEmployeeRepositoryMock).save(dbRdEmployee);
    }

    @Test
    public void testRemoveEmployeeCurrentOrderIsSetToNull() {
        employeeService.removeEmployeeFromCompany(dbRdEmployee);

        assertNull(dbRdEmployee.getCurrentOrder());

        verify(rdEmployeeRepositoryMock).save(dbRdEmployee);
    }

    @Test
    public void testRemoveEmployeeCurrentOrderCarIsSetToNull() {
        dbRdEmployee.getCurrentOrder().setOrderedCar(null);
        employeeService.removeEmployeeFromCompany(dbRdEmployee);

        assertNull(dbRdEmployee.getCurrentOrder());

        verify(rdEmployeeRepositoryMock).save(dbRdEmployee);
    }

    @Test
    public void testRemoveEmployeeInServiceIsSetToFalse() {
        employeeService.removeEmployeeFromCompany(dbRdEmployee);

        assertFalse(dbRdEmployee.isInService());

        verify(rdEmployeeRepositoryMock).save(dbRdEmployee);
    }

    //Tests for incrementing and decrementing functional levels
    @Test(expected = IllegalArgumentException.class)
    public void testIncrementFunctionalLevelEmployeeIsNull() {
        employeeService.incrementEmployeeFunctionalLevel(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncrementFunctionalLevelEmployeeIsNotFromDb() {
        RdEmployee rdEmployee = ValidDomainObjectFactory.createRdEmployee();
        employeeService.incrementEmployeeFunctionalLevel(rdEmployee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncrementFunctionalLevelEmployeeIsMaxFunctionalLevel() {
        dbRdEmployee.setFunctionalLevel(7);
        employeeService.incrementEmployeeFunctionalLevel(dbRdEmployee);
    }

    @Test
    public void testIncrementFunctionalLevelUpdatesFunctionalLevel() {
        int functionalLevel = dbRdEmployee.getFunctionalLevel();
        employeeService.incrementEmployeeFunctionalLevel(dbRdEmployee);
        assertEquals(functionalLevel + 1, dbRdEmployee.getFunctionalLevel());

        verify(rdEmployeeRepositoryMock).save(dbRdEmployee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecrementFunctionalLevelEmployeeIsNull() {
        employeeService.decrementEmployeeFunctionalLevel(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecrementFunctionalLevelEmployeeIsNotFromDb() {
        RdEmployee rdEmployee = ValidDomainObjectFactory.createRdEmployee();
        employeeService.decrementEmployeeFunctionalLevel(rdEmployee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecrementFunctionalLevelEmployeeIsMinFunctionalLevel() {
        dbRdEmployee.setFunctionalLevel(2);
        employeeService.decrementEmployeeFunctionalLevel(dbRdEmployee);
    }

    @Test
    public void testDecrementFunctionalLevelUpdatesFunctionalLevel() {
        int functionalLevel = dbRdEmployee.getFunctionalLevel();
        employeeService.decrementEmployeeFunctionalLevel(dbRdEmployee);
        assertEquals(functionalLevel - 1, dbRdEmployee.getFunctionalLevel());

        verify(rdEmployeeRepositoryMock).save(dbRdEmployee);
    }

    //Tests for setting the employee car in use
    @Test
    public void testSetEmployeeCarInUseAllOk(){
        dbRdEmployee.getCurrentOrder().getOrderedCar().setCarStatus(CarStatus.PENDING);
        employeeService.setEmployeeCarInUse(dbRdEmployee, "0-XXX-000");

        assertEquals(LocalDate.now(), dbRdEmployee.getCurrentOrder().getDateReceived());
        assertTrue(dbRdEmployee.getOrderHistory().contains(dbRdEmployee.getCurrentOrder()));
        assertEquals(CarStatus.IN_USE, dbRdEmployee.getCurrentOrder().getOrderedCar().getCarStatus());

        verify(rdEmployeeRepositoryMock).save(dbRdEmployee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarLicensePlateInvalid(){
        dbRdEmployee.getCurrentOrder().getOrderedCar().setCarStatus(CarStatus.PENDING);
        employeeService.setEmployeeCarInUse(dbRdEmployee, "XXX-000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarLicensePlateExists(){
        EmployeeCar car = ValidDomainObjectFactory.createEmployeeCar();
        when(employeeCarRepositoryMock.findByLicensePlateIgnoreCase("1-ABC-000")).thenReturn(car);

        dbRdEmployee.getCurrentOrder().getOrderedCar().setCarStatus(CarStatus.PENDING);
        employeeService.setEmployeeCarInUse(dbRdEmployee, "1-ABC-000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarInUseEmployeeNull(){
        employeeService.setEmployeeCarInUse(null, "0-XXX-000");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarInUseEmployeeIdNull(){
        dbRdEmployee.setId(null);

        employeeService.setEmployeeCarInUse(dbRdEmployee, "0-XXX-000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarInUseOrderNull(){
        dbRdEmployee.setCurrentOrder(null);

        employeeService.setEmployeeCarInUse(dbRdEmployee, "0-XXX-000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarInUseEmployeeCarNull(){
        dbRdEmployee.getCurrentOrder().setOrderedCar(null);

        employeeService.setEmployeeCarInUse(dbRdEmployee, "0-XXX-000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarInUseStatusNotPending(){
        dbRdEmployee.getCurrentOrder().getOrderedCar().setCarStatus(CarStatus.IN_USE);

        employeeService.setEmployeeCarInUse(dbRdEmployee, "0-XXX-000");
    }

    //Tests for setting the employee car to the free pool
    @Test
    public void testSetEmployeeCarInFreePoolAllOk(){
        EmployeeCar orderedCar = dbRdEmployee.getCurrentOrder().getOrderedCar();
        employeeService.setEmployeeCarInFreePool(dbRdEmployee);

        assertEquals(CarStatus.NOT_USED, orderedCar.getCarStatus());
        assertNull(dbRdEmployee.getCurrentOrder());

        verify(rdEmployeeRepositoryMock).save(dbRdEmployee);
        verify(employeeCarRepositoryMock).save(orderedCar);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarInFreePoolEmployeeIsNull(){
        employeeService.setEmployeeCarInFreePool(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarInFreePoolEmployeeIdIsNull(){
        dbRdEmployee.setId(null);
        employeeService.setEmployeeCarInFreePool(dbRdEmployee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarInFreePoolOrderIsNull(){
        dbRdEmployee.setCurrentOrder(null);

        employeeService.setEmployeeCarInFreePool(dbRdEmployee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarInFreePoolCarIsNull(){
        dbRdEmployee.getCurrentOrder().setOrderedCar(null);

        employeeService.setEmployeeCarInFreePool(dbRdEmployee);
    }

    //Tests for setting the employee car to removed
    @Test
    public void testSetEmployeeCarRemovedAllOk(){
        EmployeeCar orderedCar = dbRdEmployee.getCurrentOrder().getOrderedCar();
        employeeService.setEmployeeCarRemoved(dbRdEmployee);

        assertEquals(CarStatus.REMOVED, orderedCar.getCarStatus());
        assertNull(dbRdEmployee.getCurrentOrder());

        verify(rdEmployeeRepositoryMock).save(dbRdEmployee);
        verify(employeeCarRepositoryMock).save(orderedCar);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarRemovedEmployeeIsNull(){
        employeeService.setEmployeeCarRemoved(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarRemovedEmployeeIdIsNull(){
        dbRdEmployee.setId(null);
        employeeService.setEmployeeCarRemoved(dbRdEmployee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarRemovedOrderIsNull(){
        dbRdEmployee.setCurrentOrder(null);

        employeeService.setEmployeeCarRemoved(dbRdEmployee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmployeeCarRemovedCarIsNull(){
        dbRdEmployee.getCurrentOrder().setOrderedCar(null);

        employeeService.setEmployeeCarRemoved(dbRdEmployee);
    }

    //Test for finding employee based on license plate
    @Test(expected = IllegalArgumentException.class)
    public void testFindEmployeeByLicensePlateLicensePlateEmpty(){
        employeeService.findEmployeeByLicensePlateOfCurrentCar("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindEmployeeByLicensePlateLicensePlateNull(){
        employeeService.findEmployeeByLicensePlateOfCurrentCar(null);
    }

    @Test
    public void testFindEmployeeByLicensePlateAllOk(){
        when(rdEmployeeRepositoryMock.findRdEmployeeByCurrentOrder_OrderedCar_LicensePlate(dbRdEmployee.getCurrentOrder().getOrderedCar().getLicensePlate())).thenReturn(dbRdEmployee);

        String licensePlate = dbRdEmployee.getCurrentOrder().getOrderedCar().getLicensePlate();
        assertEquals(dbRdEmployee, employeeService.findEmployeeByLicensePlateOfCurrentCar(licensePlate));
        verify(rdEmployeeRepositoryMock).findRdEmployeeByCurrentOrder_OrderedCar_LicensePlate(licensePlate);
    }

    @Test
    public void testFindEmployeeByLicensePlateNotFound(){
        String licensePlate = "0-XXX-000";
        assertNull(employeeService.findEmployeeByLicensePlateOfCurrentCar(licensePlate));
        verify(rdEmployeeRepositoryMock).findRdEmployeeByCurrentOrder_OrderedCar_LicensePlate(licensePlate);
    }

    //Tests findAllRdEmployeesInService & findRdEmployee
    @Test
    public void testFindAllRdEmployeesInServiceCallsRepository(){
        List<RdEmployee> employees = new ArrayList<>(Collections.singletonList(dbRdEmployee));
        when(rdEmployeeRepositoryMock.findAllEmployeesInService()).thenReturn(employees);

        assertEquals(employees, employeeService.findAllRdEmployeesInService());

        verify(rdEmployeeRepositoryMock).findAllEmployeesInService();
    }

    @Test
    public void testFindRdEmployeeById(){
        when(rdEmployeeRepositoryMock.findOne(1l)).thenReturn(dbRdEmployee);

        assertEquals(dbRdEmployee, employeeService.findRdEmployee(1l));

        verify(rdEmployeeRepositoryMock).findOne(1l);
    }

    @Test
    public void testFindAllRdEmployeesInServiceWithPendingRequestOrNoCarReturnsEmployees(){
        List<RdEmployee> employees = new ArrayList<>(Arrays.asList(dbRdEmployee, dbRdEmployee));
        when(rdEmployeeRepositoryMock.findAllEmployeesInServiceWithPendingRequestOrNoCar()).thenReturn(employees);

        assertEquals(employees, employeeService.findAllRdEmployeesInServiceWithPendingRequestOrNoCar());

        verify(rdEmployeeRepositoryMock).findAllEmployeesInServiceWithPendingRequestOrNoCar();
    }

    @Test
    public void testFindAllRdEmployeesInServiceWithPendingRequestOrNoCarReturnsEmptyList(){
        assertTrue(employeeService.findAllRdEmployeesInServiceWithPendingRequestOrNoCar().isEmpty());
        verify(rdEmployeeRepositoryMock).findAllEmployeesInServiceWithPendingRequestOrNoCar();
    }
}
