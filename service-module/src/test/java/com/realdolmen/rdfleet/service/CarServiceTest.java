package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.config.JpaConfig;
import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.domain.Pack;
import com.realdolmen.rdfleet.repositories.CarRepository;
import com.realdolmen.rdfleet.repositories.PackRepository;
import com.realdolmen.rdfleet.service.util.ValidDomainObjectFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
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
@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {

    private CarRepository carRepositoryMock;
    private PackRepository packRepositoryMock;

    private CarService carService;
    private List<Car> allCars;
    private Car car;

    @Before
    public void initialize(){
        carRepositoryMock = mock(CarRepository.class);
        packRepositoryMock = mock(PackRepository.class);

        carService = new CarService();
        carService.setCarRepository(carRepositoryMock);
        carService.setPackRepository(packRepositoryMock);

        Car car1 = ValidDomainObjectFactory.createCar();
        Car car2 = ValidDomainObjectFactory.createCar();
        car2.setMake("Audi");
        car2.setModel("A1");
        car2.setFunctionalLevel(2);

        allCars = new ArrayList<>(Arrays.asList(car1, car2));
        when(carRepositoryMock.findAllOrderableCars()).thenReturn(allCars);

        car = ValidDomainObjectFactory.createCar();
        car.setId(1000l);
    }

    @Test
    public void testFindAllOrderableCarsFindsAllCars(){
        assertEquals(allCars, carService.findAllOrderableCars());

        verify(carRepositoryMock).findAllOrderableCars();
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

    @Test
    public void testMakeCarNotOrderableSetsOrderableFalse(){
        assertTrue(car.isOrderable());
        carService.makeCarNotOrderable(car);
        assertFalse(car.isOrderable());
        verify(carRepositoryMock).save(car);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMakeCarNotORderableCarIsNull(){
        carService.makeCarNotOrderable(null);
    }

    @Test
    public void findByIdAndIsOrderableFindsCar(){
        when(carRepositoryMock.findOne(1l)).thenReturn(car);

        assertEquals(car, carService.findByIdAndIsOrderable(1l));
        verify(carRepositoryMock).findOne(1l);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdNoCarFound(){
        carService.findByIdAndIsOrderable(2l);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdCarNotOrderable(){
        car.setOrderable(false);
        when(carRepositoryMock.findOne(1l)).thenReturn(car);

        carService.findByIdAndIsOrderable(1l);
    }

    @Test
    public void testFindByIdFindsCar(){
        when(carRepositoryMock.findOne(1l)).thenReturn(car);

        assertEquals(car, carService.findById(1l));
        verify(carRepositoryMock).findOne(1l);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdIdIsNull(){
        carService.findById(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdIdNegative(){
        carService.findById(-5l);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdNoneFound(){
        carService.findById(2l);
    }

    @Test
    public void testFindAllPacks(){
        Pack pack1 = ValidDomainObjectFactory.createPack();
        Pack pack2 = ValidDomainObjectFactory.createPack();
        List<Pack> packs = new ArrayList<>(Arrays.asList(pack1, pack2));
        when(packRepositoryMock.findAll()).thenReturn(packs);

        assertEquals(packs, carService.findAllPacks());
        verify(packRepositoryMock).findAll();
    }

    @Test
    public void testFindAllPacksNoneFound(){
        assertEquals(0, carService.findAllPacks().size());
        verify(packRepositoryMock).findAll();
    }
}
