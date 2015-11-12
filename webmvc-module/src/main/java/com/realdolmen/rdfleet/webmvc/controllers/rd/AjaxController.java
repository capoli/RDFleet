package com.realdolmen.rdfleet.webmvc.controllers.rd;

import com.realdolmen.rdfleet.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Controller
@RequestMapping("/api")
public class AjaxController {
    @Autowired
    private EmployeeService employeeService;

    //TODO: .com wordt niet meegenomen
    @RequestMapping(value = "/canOrderCar/{email}", method = RequestMethod.GET)
    public @ResponseBody boolean getCanOrderCar(@PathVariable("email") String email) {
        return employeeService.employeeCanOrderNewCar(String.format("%s.com", email));
    }

}
