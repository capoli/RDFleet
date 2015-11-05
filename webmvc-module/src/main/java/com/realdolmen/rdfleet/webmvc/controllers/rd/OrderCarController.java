package com.realdolmen.rdfleet.webmvc.controllers.rd;

import com.realdolmen.rdfleet.domain.EmployeeCar;
import com.realdolmen.rdfleet.service.CarOptionService;
import com.realdolmen.rdfleet.service.CarService;
import com.realdolmen.rdfleet.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rd/cars")
@Secured({"RdEmployee", "FleetEmployee"})
@SessionAttributes("employeeCar")
public class OrderCarController {
    @Autowired
    private CarService carService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CarOptionService carOptionService;

    @ModelAttribute("employeeCar")
    public EmployeeCar getInitializedEmployeeCar() {
        return new EmployeeCar();
    }

    @RequestMapping(value = "/{id}/order", method = RequestMethod.GET)
    public String getOrderNewCar(@PathVariable("id") Long id, Model model, @ModelAttribute("employeeCar") EmployeeCar employeeCar) {
        if(!canOrderNewCar()) return "redirect:/index";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        employeeCar.setSelectedCar(carService.findById(id));
        model.addAttribute("functionalLevel", employeeService.getFunctionalLevelByEmail(auth.getName()));
        model.addAttribute("carOptions", carOptionService.findAllCarOptions());
        return "rd/car.order";
    }

    @RequestMapping(value="/order", method = RequestMethod.POST)
    public String postOrderNewCar(@ModelAttribute("employeeCar") EmployeeCar employeeCar) {
        if(!canOrderNewCar()) return "redirect:/index";
        return "redirect:/rd/cars/summary";
    }

    @RequestMapping(value="/summary", method = RequestMethod.GET)
    public String getSummaryCar(@ModelAttribute("employeeCar") EmployeeCar employeeCar, Model model) {
        if(!canOrderNewCar()) return "redirect:/index";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("order", employeeService.createOrderForEmployee(auth.getName(), employeeCar));
        model.addAttribute("functionalLevel", employeeService.getFunctionalLevelByEmail(auth.getName()));
        return "rd/car.summary";
    }

    private boolean canOrderNewCar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return employeeService.employeeCanOrderNewCar(auth.getName());
    }
}
