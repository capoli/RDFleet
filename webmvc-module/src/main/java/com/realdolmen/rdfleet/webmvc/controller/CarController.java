package com.realdolmen.rdfleet.webmvc.controller;

import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by OCPAX79 on 29/10/2015.
 */
@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    private CarRepository carRepository;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Car> findAllCars() {
        return carRepository.findAll();
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void updateCar(@RequestBody Car car){
        carRepository.save(car);
    }
}
