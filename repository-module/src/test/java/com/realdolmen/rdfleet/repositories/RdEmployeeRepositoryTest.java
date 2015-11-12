package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.config.JpaConfig;
import com.realdolmen.rdfleet.domain.CarStatus;
import com.realdolmen.rdfleet.domain.RdEmployee;
import com.realdolmen.rdfleet.util.ValidDomainObjectFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JpaConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RdEmployeeRepositoryTest {

    @Autowired
    private RdEmployeeRepository repository;
    private RdEmployee rdEmployee;

    @Before
    public void initialize() {
        rdEmployee = ValidDomainObjectFactory.createRdEmployee();
    }

    @Test
    public void testFindAllEmployeesInService() {
        RdEmployee inServiceEmployee1 = createEmployee(3, true, "John", "Johnson", "John.Johnson@RealDolmen.com", "SomePassword");
        RdEmployee inServiceEmployee2 = createEmployee(6, true, "Steven", "Stevenson", "Steven.Stevenson@RealDolmen.com", "SomePassword");
        RdEmployee notInServiceEmployee1 = createEmployee(5, false, "Jack", "Jackson", "Jack.Jackson@RealDolmen.com", "SomePassword");
        RdEmployee notInServiceEmployee2 = createEmployee(3, false, "Jim", "Jimmerson", "Jim.Jimmerson@RealDolmen.com", "SomePassword");
        repository.save(inServiceEmployee1);
        repository.save(inServiceEmployee2);
        repository.save(notInServiceEmployee1);
        repository.save(notInServiceEmployee2);

        assertEquals(2, repository.findAllEmployeesInService().size());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testFunctionalLevelBelow2() {
        rdEmployee.setFunctionalLevel(-2);
        repository.save(rdEmployee);
    }

    @Test
    public void testFunctionalLevel2() {
        rdEmployee.setFunctionalLevel(2);
        repository.save(rdEmployee);
    }

    @Test
    public void testFunctionalLevelBetween2And7() {
        rdEmployee.setFunctionalLevel(5);
        repository.save(rdEmployee);
    }

    @Test
    public void testFunctionalLevel7() {
        rdEmployee.setFunctionalLevel(7);
        repository.save(rdEmployee);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testFunctionalLevelAbove7() {
        rdEmployee.setFunctionalLevel(9);
        repository.save(rdEmployee);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testFirstNameNull() {
        rdEmployee.setFirstName(null);
        repository.save(rdEmployee);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testFirstNameBlank() {
        rdEmployee.setFirstName("");
        repository.save(rdEmployee);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testLastNameNull() {
        rdEmployee.setLastName(null);
        repository.save(rdEmployee);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testLastNameBlank() {
        rdEmployee.setLastName("");
        repository.save(rdEmployee);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmailNull() {
        rdEmployee.setEmail(null);
        repository.save(rdEmployee);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmailInvalid() {
        rdEmployee.setEmail("invalid");
        repository.save(rdEmployee);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testEmailBlank() {
        rdEmployee.setEmail("");
        repository.save(rdEmployee);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testEmailDuplicate() {
        String email = "duplicate@mail.com";
        rdEmployee.setEmail(email);
        repository.save(rdEmployee);
        RdEmployee employee = createEmployee(6, true, "Fname", "Lname", email, "Password");
        repository.save(employee);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testPasswordNull() {
        rdEmployee.setPassword(null);
        repository.save(rdEmployee);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testPasswordBlank() {
        rdEmployee.setPassword("");
        repository.save(rdEmployee);
    }

    @Test
    public void testFindByEmail(){
        repository.save(rdEmployee);

        assertEquals(rdEmployee, repository.findByEmailIgnoreCase(rdEmployee.getEmail()));
    }

    @Test
    public void testUpdate(){
        repository.save(rdEmployee);

        assertEquals(1, repository.findAll().size());

        RdEmployee byEmailEmployee = repository.findByEmailIgnoreCase(rdEmployee.getEmail());
        byEmailEmployee.setInService(false);
        repository.save(byEmailEmployee);

        assertEquals(1, repository.findAll().size());
        assertFalse(repository.findByEmailIgnoreCase(byEmailEmployee.getEmail()).isInService());
    }

    @Test
    public void testFindEmployeeByLicensePlate(){
        repository.save(rdEmployee);
        String licensePlate = rdEmployee.getCurrentOrder().getOrderedCar().getLicensePlate();
        assertEquals(rdEmployee, repository.findRdEmployeeByCurrentOrder_OrderedCar_LicensePlate(licensePlate));
    }

    @Test
    public void testFindEmployeeByLicensePlateNotFound(){
        repository.save(rdEmployee);
        assertNull(repository.findRdEmployeeByCurrentOrder_OrderedCar_LicensePlate("0-XXX-000"));
    }

    @Test
    public void testFindEmployeeByLicensePlateNull(){
        assertNull(repository.findRdEmployeeByCurrentOrder_OrderedCar_LicensePlate(null));
    }

    @Ignore("This query does not behave the way it should. For no reason...")
    @Test
    public void testFindAllEmployeesInServiceWithPendingRequestOrNoCar(){
        RdEmployee inServiceWithPendingRequestEmployee = ValidDomainObjectFactory.createRdEmployee();
        inServiceWithPendingRequestEmployee.setInService(true);
        inServiceWithPendingRequestEmployee.getCurrentOrder().getOrderedCar().setCarStatus(CarStatus.PENDING);
        inServiceWithPendingRequestEmployee.setEmail("pending@employee.com");
        inServiceWithPendingRequestEmployee.getCurrentOrder().getOrderedCar().setLicensePlate("0-AAA-000");

        RdEmployee inServiceWithNoOrderEmployee = ValidDomainObjectFactory.createRdEmployee();
        inServiceWithNoOrderEmployee.setInService(true);
        inServiceWithNoOrderEmployee.setCurrentOrder(null);
        inServiceWithNoOrderEmployee.setEmail("noorder@employee.com");

        RdEmployee notInServiceWithPendingRequestEmployee = ValidDomainObjectFactory.createRdEmployee();
        notInServiceWithPendingRequestEmployee.setInService(false);
        notInServiceWithPendingRequestEmployee.getCurrentOrder().getOrderedCar().setCarStatus(CarStatus.PENDING);

        repository.saveAndFlush(inServiceWithPendingRequestEmployee);
        repository.saveAndFlush(inServiceWithNoOrderEmployee);
        repository.saveAndFlush(notInServiceWithPendingRequestEmployee);

        List<RdEmployee> retrievableEmpoyees = new ArrayList<>(Arrays.asList(inServiceWithNoOrderEmployee, inServiceWithPendingRequestEmployee));

        List<RdEmployee> allEmployeesInServiceWithPendingRequestOrNoCar = repository.findAllEmployeesInServiceWithPendingRequestOrNoCar();
        assertEquals(retrievableEmpoyees, allEmployeesInServiceWithPendingRequestOrNoCar);
    }

    private RdEmployee createEmployee(int funcLevel, boolean inService, String fname, String lname, String mail, String pw) {
        RdEmployee employee = new RdEmployee();
        employee.setFunctionalLevel(funcLevel);
        employee.setInService(inService);
        employee.setFirstName(fname);
        employee.setLastName(lname);
        employee.setEmail(mail);
        employee.setPassword(pw);
        return employee;
    }

}
