package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.config.JpaConfig;
import com.realdolmen.rdfleet.domain.EmployeeCar;
import com.realdolmen.rdfleet.util.ValidDomainObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by JSTAX29 on 29/10/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JpaConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeCarRepositoryTest {
    @Autowired
    private EmployeeCarRepository repository;

    private EmployeeCar employeeCar;

    @Before
    public void initialize() {
        employeeCar = ValidDomainObjectFactory.createEmployeeCar();
    }

    @Test
    public void testFindEmployeeCarByLicensePlate() {
        repository.save(employeeCar);

        String licensePlate = employeeCar.getLicensePlate();
        assertEquals(employeeCar, repository.findByLicensePlateIgnoreCase(licensePlate));
    }

    @Test
    public void testFindEmployeeCarByLicensePlateNotFound() {
        repository.save(employeeCar);

        String unknownLicensePlate = "XXX-999";
        assertNull(repository.findByLicensePlateIgnoreCase(unknownLicensePlate));

    }

    @Test
    public void testFindEmployeeCarByLicensePlateIgnoresCase() {
        repository.save(employeeCar);

        String licensePlateLower = employeeCar.getLicensePlate().toLowerCase();
        assertEquals(employeeCar, repository.findByLicensePlateIgnoreCase(licensePlateLower));
        String licensePlateUpper = employeeCar.getLicensePlate().toUpperCase();
        assertEquals(employeeCar, repository.findByLicensePlateIgnoreCase(licensePlateUpper));
    }

    @Test(expected = ConstraintViolationException.class)
    public void testMileageIsNegative() {
        employeeCar.setMileage(-500);
        repository.save(employeeCar);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCarStatusNull() {
        employeeCar.setCarStatus(null);
        repository.save(employeeCar);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSelectedCarNull() {
        employeeCar.setSelectedCar(null);
        repository.save(employeeCar);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testLicensePlateNull() {
        employeeCar.setLicensePlate(null);
        repository.save(employeeCar);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testLicensePlateBlank() {
        employeeCar.setLicensePlate("");
        repository.save(employeeCar);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testDuplicateLicensePlate() {
        repository.save(employeeCar);
        EmployeeCar employeeCar1 = ValidDomainObjectFactory.createEmployeeCar();
        repository.save(employeeCar1);
    }

}
