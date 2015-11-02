package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by JSTAX29 on 1/11/2015.
 */
@Service
public class CarService {
    private CarRepository carRepository;

    @Autowired
    public void setCarRepository(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> findAllCars() {
        return carRepository.findAll();
    }

    public void updateCar(Car car) {
        if(car.getId() == null)
            throw new IllegalArgumentException("The car cannot be updated because it did not originate from the database.");

        carRepository.save(car);
    }

    public void createCar(Car car){
        carRepository.save(car);
    }
}
