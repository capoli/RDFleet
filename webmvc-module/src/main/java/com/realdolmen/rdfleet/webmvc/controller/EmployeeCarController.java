package com.realdolmen.rdfleet.webmvc.controller;

import com.realdolmen.rdfleet.domain.EmployeeCar;
import com.realdolmen.rdfleet.service.EmployeeCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by JSTAX29 on 30/10/2015.
 */
@RestController
@RequestMapping("/api/employeecars")
public class EmployeeCarController {
    @Autowired
    private EmployeeCarService employeeCarService;

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<EmployeeCar> findAllEmployeeCars() {
        return employeeCarService.findAllEmployeeCars();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{licensePlate}")
    public EmployeeCar findEmployeeCarByLicensePlate(@PathVariable String licensePlate){
        return employeeCarService.findEmployeeCarByLicensePlate(licensePlate);
    }

}
