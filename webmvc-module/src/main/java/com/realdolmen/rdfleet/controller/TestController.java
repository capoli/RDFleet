package com.realdolmen.rdfleet.controller;

import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by OCPAX79 on 29/10/2015.
 */
@RestController
@RequestMapping("/api/cars")
public class TestController {
    @Autowired
    private CarRepository carRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Car> findItems() {
        return carRepository.findAll();
    }
}
