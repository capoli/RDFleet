package com.realdolmen.rdfleet.repositories;

import com.realdolmen.rdfleet.config.JpaConfig;
import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.domain.FuelType;
import com.realdolmen.rdfleet.domain.TyreType;
import com.realdolmen.rdfleet.util.ValidDomainObjectFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by JSTAX29 on 27/10/2015.
 * Tests the
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JpaConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CarRepositoryTest {
    @Autowired
    private CarRepository carRepository;
    private Car car;

    @Before
    public void initialize() {
        car = ValidDomainObjectFactory.createCar();
    }

    @Test
    public void testCarRepository() {
        carRepository.save(car);
        assertNotNull(car.getId());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCarMakeNull() {
        car.setMake(null);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCarMakeEmpty() {
        car.setMake("");
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testModelNull() {
        car.setModel(null);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testModelEmpty() {
        car.setModel("");
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testFunctionalLevelNegative() {
        car.setFunctionalLevel(-1);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testFunctionalLevel1() {
        car.setFunctionalLevel(1);
        carRepository.save(car);
    }

    @Test
    public void testFunctionalLevel2() {
        car.setFunctionalLevel(2);
        carRepository.save(car);
        assertNotNull(car.getId());
    }

    @Test
    public void testFunctionalLevel7() {
        car.setFunctionalLevel(7);
        carRepository.save(car);
        assertNotNull(car.getId());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testFunctionalLevelAbove7() {
        car.setFunctionalLevel(8);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testTyreTypeNull() {
        car.setTyreType(null);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testFuelTypeNull() {
        car.setFuelType(null);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testIdealKmNegative() {
        car.setIdealKm(-5);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testMaxKmNegative() {
        car.setMaxKm(-5);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testListPriceNull() {
        car.setListPrice(null);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testListPriceNegative() {
        car.setListPrice(BigDecimal.valueOf(-10000));
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testListPriceTooManyFractions() {
        car.setListPrice(BigDecimal.valueOf(23548.685));
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testBenefitNull() {
        car.setBenefit(null);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testBenefitNegative() {
        car.setBenefit(BigDecimal.valueOf(-10000));
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testBenefitTooManyFractions() {
        car.setBenefit(BigDecimal.valueOf(23548.685));
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testAmountUpgradeNull() {
        car.setAmountUpgrade(null);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testAmountUpgradeNegative() {
        car.setAmountUpgrade(BigDecimal.valueOf(-10000));
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testAmountUpgradeTooManyFractions() {
        car.setAmountUpgrade(BigDecimal.valueOf(23584.218));
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testAmountDowngradeNull() {
        car.setAmountDowngrade(null);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testAmountDowngradeNegative() {
        car.setAmountDowngrade(BigDecimal.valueOf(-5228));
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testAmountDowngradeTooManyFractions() {
        car.setAmountDowngrade(BigDecimal.valueOf(2158.856));
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCo2Negative() {
        car.setCo2(-5056);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testFiscHpNegative() {
        car.setFiscHp(-558);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testTimeOfDeliveryNull() {
        car.setTimeOfDeliveryInDays(null);
        carRepository.save(car);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testTimeOfDeliveryNegative() {
        car.setTimeOfDeliveryInDays(Duration.ofDays(-5254));
        carRepository.save(car);
    }

    @Test
    public void testFindAllOrderableCars(){
        carRepository.save(car);
        Car notOrderableCar = ValidDomainObjectFactory.createCar();
        notOrderableCar.setOrderable(false);
        carRepository.save(notOrderableCar);

        assertEquals(1, carRepository.findAllOrderableCars().size());
    }

    @Test
    public void testFindAllOrderableCarsNoneFound(){
        assertEquals(0, carRepository.findAllOrderableCars().size());
    }

    @Test
    public void testFindCarGivesRightAmountOfPacks(){
        assertEquals(2, car.getPacks().size());
        Car savedCar = carRepository.save(car);
        Car newCar = carRepository.findOne(savedCar.getId());
        assertEquals(2, newCar.getPacks().size());
    }

}
