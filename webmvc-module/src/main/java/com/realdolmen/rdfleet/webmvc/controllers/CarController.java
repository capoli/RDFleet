package com.realdolmen.rdfleet.webmvc.controllers;

import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by cda5732 on 16/04/2015.
 */
@Controller
@RequestMapping("/fleetemployee/cars")
public class CarController {
    @Autowired
    private CarService carService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Car> cars() {
        return carService.findAllCars();
    }
}
