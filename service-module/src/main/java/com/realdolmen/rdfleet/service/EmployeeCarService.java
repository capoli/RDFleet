package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.domain.CarStatus;
import com.realdolmen.rdfleet.domain.EmployeeCar;
import com.realdolmen.rdfleet.repositories.EmployeeCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Created by JSTAX29 on 1/11/2015.
 */
@Service
public class EmployeeCarService {
    private EmployeeCarRepository employeeCarRepository;

    @Autowired
    public void setEmployeeCarRepository(EmployeeCarRepository employeeCarRepository) {
        this.employeeCarRepository = employeeCarRepository;
    }

    public List<EmployeeCar> findAllEmployeeCars() {
        return employeeCarRepository.findAll();
    }

    public EmployeeCar findEmployeeCarByLicensePlate(String licensePlate){
        return employeeCarRepository.findByLicensePlateIgnoreCase(licensePlate);
    }

}
