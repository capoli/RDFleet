package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.domain.RdEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RdEmployeeRepository extends JpaRepository<RdEmployee, Long> {
    @Query("select e from RdEmployee e where e.inService = true")
    List<RdEmployee> findAllEmployeesInService();

    RdEmployee findByEmailIgnoreCase(String email);

    RdEmployee findRdEmployeeByCurrentOrder_OrderedCar_LicensePlate(String licensePlate);
// or e.currentOrder.orderedCar.carStatus = 'PENDING'
    @Query("select e from RdEmployee e where e.inService = true and (e.currentOrder is null or e.currentOrder.orderedCar.carStatus = 'PENDING')")
    List<RdEmployee> findAllEmployeesInServiceWithPendingRequestOrNoCar();
}
