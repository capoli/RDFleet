package com.realdolmen.rdfleet.webmvc.controllers.fleet;

import com.realdolmen.rdfleet.domain.RdEmployee;
import com.realdolmen.rdfleet.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMappingName;

/**
 * Created by JSTAX29 on 2/11/2015.
 */
@Controller
@RequestMapping("/fleet/employees")
//@Secured("FleetEmployee")
public class RdEmployeeManagementController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(method = RequestMethod.GET)
    public String employees(Model model) {
        model.addAttribute("employeeList", employeeService.findAllRdEmployeesInService());
        return "fleet/employee.list";
    }

    @RequestMapping(value = "/{id}/remove", method = RequestMethod.GET)
    public String removeFromService(@PathVariable Long id, Model model){
        RdEmployee rdEmployee = employeeService.findRdEmployee(id);

        if(id == null || rdEmployee == null){
            model.addAttribute("error", "The employee with the provided id could not be found.");
            model.addAttribute("employeeList", employeeService.findAllRdEmployeesInService());
            return "fleet/employee.list";
        }else{
            employeeService.removeEmployeeFromCompany(rdEmployee);
        }

        return "redirect:" + fromMappingName("REMC#employees").build();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateEmployee(@ModelAttribute RdEmployee rdEmployee, BindingResult errors, Model model){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!! Came into method !!!!!!!!!!!!!!!!!!!!!!");
        return "redirect:" + fromMappingName("REMC#employees").build();
    }


}
