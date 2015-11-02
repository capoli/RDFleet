package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.config.JpaConfig;
import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.repositories.CarRepository;
import com.realdolmen.rdfleet.service.util.ValidDomainObjectFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by JSTAX29 on 2/11/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JpaConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CarServiceTest {
    @Autowired
    private CarRepository carRepository;

    private CarRepository carRepositoryMock;
    private CarService carService;
    private List<Car> allCars;
    private Car car;

    @Before
    public void initialize(){
        carRepositoryMock = mock(CarRepository.class);

        carService = new CarService();
        carService.setCarRepository(carRepositoryMock);

        Car car1 = ValidDomainObjectFactory.createCar();
        Car car2 = ValidDomainObjectFactory.createCar();
        car2.setMake("Audi");
        car2.setModel("A1");
        car2.setFunctionalLevel(2);

        allCars = new ArrayList<>(Arrays.asList(car1, car2));
        when(carRepositoryMock.findAll()).thenReturn(allCars);

        Car car = ValidDomainObjectFactory.createCar();
        this.car = carRepository.save(car);
    }

    @Test
    public void testFindAllCarsFindsAllCars(){
        assertEquals(allCars, carService.findAllCars());

        verify(carRepositoryMock).findAll();
    }

    @Test
    public void testCreateCarCallsRepository(){
        Car car = ValidDomainObjectFactory.createCar();
        carService.createCar(car);
        verify(carRepositoryMock).save(car);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCarGetsCarNotFromDb(){
        Car car = ValidDomainObjectFactory.createCar();
        carService.updateCar(car);
    }

    @Test
    public void testUpdateCarUpdates(){
        car.setDescription("Some different description");
        carService.updateCar(car);
        verify(carRepositoryMock).save(car);
    }

}
