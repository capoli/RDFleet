package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by JSTAX29 on 27/10/2015.
 */

public interface CarRepository extends JpaRepository<Car, Long> {
}
