package com.realdolmen.rdfleet.webmvc.controllers.fleet;

import com.realdolmen.rdfleet.domain.CarStatus;
import com.realdolmen.rdfleet.domain.EmployeeCar;
import com.realdolmen.rdfleet.domain.RdEmployee;
import com.realdolmen.rdfleet.service.EmployeeCarService;
import com.realdolmen.rdfleet.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMappingName;

/**
 * Created by JSTAX29 on 4/11/2015.
 */
@Controller
@RequestMapping("/fleet/employees/car")
public class EmployeeCarManagementController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/{employeeId}", method = RequestMethod.GET)
    public String viewEmployeeCar(@PathVariable Long employeeId, Model model) {
        RdEmployee rdEmployee = employeeService.findRdEmployee(employeeId);
        if(rdEmployee == null){
            model.addAttribute("error", "The employee id " + employeeId + " was not found in the system.");
            model.addAttribute("employeeList", employeeService.findAllRdEmployeesInService());
            return "fleet/employee.list";
        }
        model.addAttribute("employee", rdEmployee);
        return "fleet/employee.car.detail";
    }

    @RequestMapping(value = "/status-in-use", method = RequestMethod.POST)
    public String setCarStatusInUse(@RequestParam("employeeId") Long employeeId, Model model){
        if(employeeId == null){
            model.addAttribute("error", "Employee id must be given.");
        }

        RdEmployee employee = employeeService.findRdEmployee(employeeId);

        if(employee == null){
            model.addAttribute("error", "Employee was not found.");
            return "fleet/employee.list";
        }

        try{
            employeeService.setEmployeeCarInUse(employee);
        }catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
        }

        if(model.containsAttribute("error")){
            model.addAttribute("employee", employee);
            return "fleet/employee.car.detail";
        }

        return "redirect:" + fromMappingName("ECMC#viewEmployeeCar").arg(0, employee.getId()).build();
    }


    @RequestMapping(value = "/move-free-pool", method = RequestMethod.POST)
    public String setCarToFreePool(@RequestParam("employeeId") Long employeeId, Model model){
        return setCarStatusTo(employeeId, model, CarStatus.NOT_USED);
    }


    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public String setCarStatusRemoved(@RequestParam("employeeId") Long employeeId, Model model){
        return setCarStatusTo(employeeId, model, CarStatus.REMOVED);
    }

    private String setCarStatusTo(Long employeeId, Model model, CarStatus status) {
        if(employeeId == null){
            model.addAttribute("error", "Employee id must be given.");
        }

        RdEmployee employee = employeeService.findRdEmployee(employeeId);

        if(employee == null){
            model.addAttribute("error", "Employee was not found.");
            return "fleet/employee.list";
        }

        try{
            switch (status){
                case REMOVED:
                    employeeService.setEmployeeCarRemoved(employee);
                    break;
                case NOT_USED:
                    employeeService.setEmployeeCarInFreePool(employee);
                    break;
                default:
                    throw new IllegalArgumentException("An invalid status was provided.");
            }
        }catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
        }

        if(model.containsAttribute("error")){
            model.addAttribute("employee", employee);
            return "fleet/employee.car.detail";
        }

        return "redirect:" + fromMappingName("REMC#employees").build();
    }
}
