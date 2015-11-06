package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by JSTAX29 on 27/10/2015.
 */

public interface CarRepository extends JpaRepository<Car, Long> {
    //TODO: test
    @Query("select e from Car e where e.orderable = true and e.functionalLevel between (:funcLvl -1) and (:funcLvl +1)")
    List<Car> findAllCarsByFunctionalLevelAndIsOrderable(@Param("funcLvl") int funcLvl);
}
