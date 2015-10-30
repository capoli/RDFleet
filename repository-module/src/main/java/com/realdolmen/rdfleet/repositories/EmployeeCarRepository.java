package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.domain.EmployeeCar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by JSTAX29 on 28/10/2015.
 */
public interface EmployeeCarRepository extends JpaRepository<EmployeeCar, Long> {
    EmployeeCar findByLicensePlateIgnoreCase(String licensePlate);
}