package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.config.JpaConfig;
import com.realdolmen.rdfleet.domain.EmployeeCar;
import com.realdolmen.rdfleet.repositories.EmployeeCarRepository;
import com.realdolmen.rdfleet.service.util.ValidDomainObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by JSTAX29 on 2/11/2015.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = JpaConfig.class)
//@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(MockitoJUnitRunner.class)
public class EmployeeCarServiceTest {

    private EmployeeCarRepository employeeCarRepositoryMock;
    private EmployeeCarService employeeCarService;
    private List<EmployeeCar> allEmployeeCars;
    private EmployeeCar employeeCar2;

    @Before
    public void initialize() {
        employeeCarRepositoryMock = mock(EmployeeCarRepository.class);

        employeeCarService = new EmployeeCarService();
        employeeCarService.setEmployeeCarRepository(employeeCarRepositoryMock);

        EmployeeCar employeeCar1 = ValidDomainObjectFactory.createEmployeeCar();
        employeeCar2 = ValidDomainObjectFactory.createEmployeeCar();
        employeeCar2.setLicensePlate("1-PLC-879");

        allEmployeeCars = new ArrayList<>(Arrays.asList(employeeCar1, employeeCar2));
        when(employeeCarRepositoryMock.findAll()).thenReturn(allEmployeeCars);
    }

    @Test
    public void testFindAllEmployeeCars() {
        assertEquals(allEmployeeCars, employeeCarService.findAllEmployeeCars());

        verify(employeeCarRepositoryMock).findAll();
    }

    @Test
    public void testFindEmployeeCarByLicensePlate() {
        when(employeeCarRepositoryMock.findByLicensePlateIgnoreCase("1-PLC-879")).thenReturn(employeeCar2);

        assertEquals(employeeCar2, employeeCarService.findEmployeeCarByLicensePlate("1-PLC-879"));

        verify(employeeCarRepositoryMock).findByLicensePlateIgnoreCase("1-PLC-879");
    }

}