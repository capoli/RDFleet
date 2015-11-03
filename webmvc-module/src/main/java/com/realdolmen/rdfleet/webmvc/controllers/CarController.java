package com.realdolmen.rdfleet.webmvc.controllers;

import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by cda5732 on 16/04/2015.
 */
@Controller
@RequestMapping("/rd/cars")
@Secured("RdEmployee")
public class CarController {
    @Autowired
    private CarService carService;

    @RequestMapping(method = RequestMethod.GET)
    public String cars(Model model) {
        model.addAttribute("carList", carService.findAllCars());
        return "rd/car.list";
    }


}
