package com.realdolmen.rdfleet.webmvc.controllers.fleet;

import com.realdolmen.rdfleet.domain.RdEmployee;
import com.realdolmen.rdfleet.service.EmployeeCarService;
import com.realdolmen.rdfleet.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMappingName;


@Controller
@RequestMapping("/fleet/company-cars")
public class CompanyCarManagementController {
    @Autowired
    private EmployeeCarService employeeCarService;
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(method = RequestMethod.GET)
    public String viewCompanyCars(Model model) {
        model.addAttribute("employeeCarList", employeeCarService.findAllEmployeeCars());
        return "fleet/companycar.list";
    }

    @RequestMapping(value = "/{employeeCarLicensePlate}", method = RequestMethod.GET)
    public String viewCompanyCar(@PathVariable("employeeCarLicensePlate") String employeeCarLicensePlate, Model model) {
        model.addAttribute("employeeCar", employeeCarService.findEmployeeCarByLicensePlate(employeeCarLicensePlate));
        return "fleet/companycar.detail";
    }

    @RequestMapping(value = "/employee/{employeeCarLicensePlate}", method = RequestMethod.GET)
    public String viewCompanyCarWithEmployee(@PathVariable("employeeCarLicensePlate") String employeeCarLicensePlate, Model model){
        RdEmployee employeeByLicensePlateOfCurrentCar = employeeService.findEmployeeByLicensePlateOfCurrentCar(employeeCarLicensePlate);

        if(employeeByLicensePlateOfCurrentCar != null)
            return "redirect:" + fromMappingName("ECMC#viewEmployeeCar").arg(0, employeeByLicensePlateOfCurrentCar.getId()).build();

        return "redirect:" + fromMappingName("CCMC#viewCompanyCars").build();
    }
}
