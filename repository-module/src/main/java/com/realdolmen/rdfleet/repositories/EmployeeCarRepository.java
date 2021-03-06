package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.domain.EmployeeCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeCarRepository extends JpaRepository<EmployeeCar, Long> {
    EmployeeCar findByLicensePlateIgnoreCase(String licensePlate);

    @Query("select e from EmployeeCar e where e.carStatus = 'NOT_USED'")
    List<EmployeeCar> findAllIsNotUsed();
}
