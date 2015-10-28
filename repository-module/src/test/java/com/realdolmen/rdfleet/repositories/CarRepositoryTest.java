package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.config.JpaConfig;
import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.domain.FuelType;
import com.realdolmen.rdfleet.domain.TyreType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.Assert.assertNotNull;

/**
 * Created by JSTAX29 on 27/10/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JpaConfig.class)
@ActiveProfiles("test")
public class CarRepositoryTest {
    @Autowired
    private CarRepository carRepository;

    @Test
    public void testCarRepository(){
        Car car = new Car();
        car.setFunctionalLevel(2);
        car.setMake("Audi");
        car.setModel("A1");
        car.setAmountDowngrade(BigDecimal.valueOf(2315.25));
        car.setFuelType(FuelType.DIESEL);
        car.setAmountUpgrade(BigDecimal.valueOf(0));
        car.setListPrice(BigDecimal.valueOf(25343.22));
        car.setTyreType(TyreType.ALUMINIUM);
        car.setTimeOfDeliveryInDays(Duration.ofDays(150));

        carRepository.save(car);
        assertNotNull(car.getId());
    }
}
