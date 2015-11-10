package com.realdolmen.rdfleet.service;

import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.domain.Pack;
import com.realdolmen.rdfleet.repositories.CarRepository;
import com.realdolmen.rdfleet.repositories.PackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by JSTAX29 on 1/11/2015.
 */
@Service
public class CarService {
    private CarRepository carRepository;
    private PackRepository packRepository;

    @Autowired
    public void setCarRepository(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Autowired
    public void setPackRepository(PackRepository packRepository) {
        this.packRepository = packRepository;
    }


    /**
     * Finds all cars that where orderable equals true in the database.
     * @return the list of all cars that are orderable
     */
    public List<Car> findAllOrderableCars() {
        return carRepository.findAllOrderableCars();
    }

    public void updateCar(Car car) {
        if(car.getId() == null)
            throw new IllegalArgumentException("The car cannot be updated because it did not originate from the database.");

        carRepository.save(car);
    }

    public List<Pack> findAllPacks(){
        return packRepository.findAll();
    }

    public void createCar(Car car){
        carRepository.save(car);
    }

    public Car findById(Long id) {
        if(id == null) throw new IllegalArgumentException("Car id can not be null");
        if(id < 0) throw new IllegalArgumentException("Car id can not be a negative number");
        Car car = carRepository.findOne(id);
        if(car == null) throw new IllegalArgumentException("Car object can not be null");
        return car;
    }

    /**
     * Will set the orderable property of the car to false and persist it to the database.
     * @param car the car that should no longer be orderable
     */
    public void makeCarNotOrderable(Car car) {
        if(car == null)
            throw new IllegalArgumentException("Car to be made un-orderable must be provided.");

        car.setOrderable(false);
        carRepository.save(car);
    }

    public Car findByIdAndIsOrderable(Long id) {
        Car car = findById(id);
        if(!car.isOrderable()) throw new IllegalArgumentException("Car is not orderable");
        return car;
    }
}
