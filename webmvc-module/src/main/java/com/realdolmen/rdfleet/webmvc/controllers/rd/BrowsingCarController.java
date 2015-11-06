package com.realdolmen.rdfleet.webmvc.controllers.rd;

import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.repositories.EmployeeCarRepository;
import com.realdolmen.rdfleet.service.CarService;
import com.realdolmen.rdfleet.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cda5732 on 16/04/2015.
 */
@Controller
@RequestMapping("/rd/cars")
@Secured({"RdEmployee", "FleetEmployee"})
public class BrowsingCarController {
    @Autowired
    private CarService carService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeCarRepository employeeCarRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String getNewCars(@RequestParam(value = "type", required = false) String type, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Car> cars;

        if (type != null) {
            switch (type.toLowerCase()) {
                case "order":
                    if(employeeService.employeeCanOrderNewCar(auth.getName()))
                        cars = employeeService.findCarsForEmployeeByFunctionalLevel(auth.getName());
                    else
                        cars = carService.findAllOrderableCars();
                    break;
                default:
                    cars = carService.findAllOrderableCars();
                    break;
            }
        }
        else cars = carService.findAllOrderableCars();

        model.addAttribute("carList", cars);
        return "rd/car.list";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getNewCar(@PathVariable("id") Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("car", carService.findById(id));
        model.addAttribute("canOrderNewCar", employeeService.employeeCanOrderNewCar(auth.getName(), id));
        return "rd/car.detail";
    }

    @RequestMapping(value = "/freepool", method = RequestMethod.GET)
    public String getCarsFromFreePool(Model model) {
        model.addAttribute("employeeCars", employeeCarRepository.findAllIsNotUsed());
        return "rd/freepool.list";
    }

    @RequestMapping(value = "/freepool/{id}", method = RequestMethod.GET)
    public String getFreePoolCar(@PathVariable("id") Long id) {
        return "rd/freepool.detail";
    }
}
