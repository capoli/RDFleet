package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.config.JpaConfig;
import com.realdolmen.rdfleet.domain.RdEmployee;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by JSTAX29 on 28/10/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JpaConfig.class)
@ActiveProfiles("test")
public class RdEmployeeRepositoryTest {

    @Autowired
    private RdEmployeeRepository repository;

    @Test
    public void testFindAllEmployeesInService(){
        RdEmployee inServiceEmployee1 = createEmployee(3, true, "John", "Johnson", "John.Johnson@RealDolmen.com", "SomePassword");
        RdEmployee inServiceEmployee2 = createEmployee(6, true, "Steven", "Stevenson", "Steven.Stevenson@RealDolmen.com", "SomePassword");
        RdEmployee notInServiceEmployee1 = createEmployee(5, false, "Jack", "Jackson", "Jack.Jackson@RealDolmen.com", "SomePassword");
        RdEmployee notInServiceEmployee2 = createEmployee(3, false, "Jim", "Jimmerson", "Jim.Jimmerson@RealDolmen.com", "SomePassword");
        repository.save(inServiceEmployee1);
        repository.save(inServiceEmployee2);
        repository.save(notInServiceEmployee1);
        repository.save(notInServiceEmployee2);

        Assert.assertEquals(2, repository.findAllEmployeesInService().size());
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
