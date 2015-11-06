package com.realdolmen.rdfleet.webmvc.controllers.rd;

import com.realdolmen.rdfleet.domain.EmployeeCar;
import com.realdolmen.rdfleet.domain.Order;
import com.realdolmen.rdfleet.domain.RdEmployee;
import com.realdolmen.rdfleet.service.CarOptionService;
import com.realdolmen.rdfleet.service.CarService;
import com.realdolmen.rdfleet.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;

@Controller
@RequestMapping("/rd/cars")
@Secured({"RdEmployee", "FleetEmployee"})
@SessionAttributes({"employeeCar", "order"})
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

    @ModelAttribute("order")
    public Order getInitializedOrder() {
        return new Order();
    }

    @RequestMapping(value = "/{id}/order", method = RequestMethod.GET)
    public String getOrderNewCar(@PathVariable("id") Long id, Model model, @ModelAttribute("employeeCar") EmployeeCar employeeCar) {
        if(!canOrderNewCar()) return "redirect:/index";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        employeeCar.setSelectedCar(carService.findByIdAndIsOrderable(id));
        employeeCar.setCarOptions(new ArrayList<>());
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
    public String getSummaryCar(@ModelAttribute("employeeCar") EmployeeCar employeeCar, @ModelAttribute("order") Order order, Model model) {
        if(!canOrderNewCar() || employeeCar == null) return "redirect:/index";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Order orderForEmployee = employeeService.createOrderForEmployee(auth.getName(), employeeCar);
        order.setAmountPaidByCompany(orderForEmployee.getAmountPaidByCompany());
        order.setAmountPaidByEmployee(orderForEmployee.getAmountPaidByCompany());
        order.setOrderedCar(orderForEmployee.getOrderedCar());
        model.addAttribute("functionalLevel", employeeService.getFunctionalLevelByEmail(auth.getName()));
        return "rd/car.summary";
    }

    @RequestMapping(value="/submitOrder", method = RequestMethod.POST)
    public String submitOrder(@ModelAttribute("employeeCar") EmployeeCar employeeCar,
                              @ModelAttribute("order") Order order,
                              SessionStatus status) {
        if(!canOrderNewCar() || order == null) return "redirect:/index";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        employeeService.createEmployeeCarAndDelegateOrderForEmployee(auth.getName(), order);
        status.setComplete();
        return "redirect:/index"; //TODO: success page mss
    }

    @RequestMapping(value="/cancelOrder", method = RequestMethod.POST)
    public String cancelOrder(@ModelAttribute("employeeCar") EmployeeCar employeeCar,
                              @ModelAttribute("order") Order order,
                              SessionStatus status) {
        if(!canOrderNewCar() || order == null) return "redirect:/index";
        status.setComplete();
        return "redirect:/rd/cars?type=order";
    }

    private boolean canOrderNewCar() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return employeeService.employeeCanOrderNewCar(auth.getName());
    }
}
