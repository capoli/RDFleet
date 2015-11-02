package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.domain.RdEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by JSTAX29 on 28/10/2015.
 */
public interface RdEmployeeRepository extends JpaRepository<RdEmployee, Long> {
    @Query("select e from RdEmployee e where e.inService = true")
    List<RdEmployee> findAllEmployeesInService();

    RdEmployee findByEmailIgnoreCase(String email);
}
