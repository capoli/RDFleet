package com.realdolmen.rdfleet.webmvc.controllers.fleet;

import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMappingName;

/**
 * Created by JSTAX29 on 3/11/2015.
 */
@Controller
@RequestMapping("/fleet/cars")
public class CarManagementController {
    @Autowired
    private CarService carService;
    @Autowired
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET)
    public String cars(Model model) {
        model.addAttribute("carList", carService.findAllCars());
        return "fleet/car.list";
    }

    @RequestMapping( value = "/{id}/edit", method = RequestMethod.GET)
    public String goToEditPage(@PathVariable Long id, Model model){
        model.addAttribute("car", carService.findById(id));
        return "fleet/car.detail";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String editCar(@ModelAttribute Car car, BindingResult errors, Model model) {
        validator.validate(car, errors);
        if (errors.hasErrors()) {
            model.addAttribute(carService.findAllCars());
            return "fleet/car.detail";
        }

        carService.createCar(car);
        return "redirect:" + fromMappingName("CMC#cars").build();
    }

}