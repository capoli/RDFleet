package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.config.JpaConfig;
import com.realdolmen.rdfleet.domain.CarStatus;
import com.realdolmen.rdfleet.domain.Order;
import com.realdolmen.rdfleet.domain.RdEmployee;
import com.realdolmen.rdfleet.repositories.RdEmployeeRepository;
import com.realdolmen.rdfleet.service.util.ValidDomainObjectFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by JSTAX29 on 1/11/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JpaConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeServiceTest {
    @Autowired
    private RdEmployeeRepository rdEmployeeRepository;

    private RdEmployeeRepository rdEmployeeRepositoryMock;
    private EmployeeService employeeService;

    private RdEmployee dbRdEmployee;

    @Before
    public void initialize() {
        rdEmployeeRepositoryMock = mock(RdEmployeeRepository.class);

        employeeService = new EmployeeService();
        employeeService.setRdEmployeeRepository(rdEmployeeRepositoryMock);

        RdEmployee rdEmployee = ValidDomainObjectFactory.createRdEmployee();
        dbRdEmployee = rdEmployeeRepository.save(rdEmployee);

    }

    // Assigning orders tests
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
        dbRdEmployee.setFunctionalLevel(2);

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

        verify(rdEmployeeRepositoryMock).save(dbRdEmployee);
    }

    @Test
    public void testAssignOrderSetsCarStatusPending() {
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
}
