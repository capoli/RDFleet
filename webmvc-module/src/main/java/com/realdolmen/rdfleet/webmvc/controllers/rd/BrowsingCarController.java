package com.realdolmen.rdfleet.webmvc.controllers.rd;

import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.domain.CarOption;
import com.realdolmen.rdfleet.domain.EmployeeCar;
import com.realdolmen.rdfleet.service.CarOptionService;
import com.realdolmen.rdfleet.service.CarService;
import com.realdolmen.rdfleet.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
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
    private CarOptionService carOptionService;

    @RequestMapping(method = RequestMethod.GET)
    public String getNewCars(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("carList",
                employeeService.findCarsForEmployeeByFunctionalLevel(auth.getName()));
        return "rd/car.list";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getNewCar(@PathVariable("id") Long id, Model model) {
        model.addAttribute("car", carService.findById(id));
        return "rd/car.detail";
    }

    @RequestMapping(value = "/{id}/order", method = RequestMethod.GET)
    public String getOrderNewCar(@PathVariable("id") Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("car", carService.findById(id));
        model.addAttribute("carOrder", employeeService.getNewCarOrderForEmployee(auth.getName(), id));
        model.addAttribute("carOptions", carOptionService.findAllCarOptions());
        model.addAttribute("employeeCar", new EmployeeCar());
        return "rd/car.order";
    }

    @RequestMapping(value="/{id}/overview", method = RequestMethod.POST)
    @Transactional
    public String getOverviewPage(@PathVariable("id") Long id, @Valid EmployeeCar employeeCar, BindingResult errors) {
        if (errors.hasErrors()) {
            return "/rd/cars/"+id+"/order";
        }
        return "redirect:/index";
    }
}
