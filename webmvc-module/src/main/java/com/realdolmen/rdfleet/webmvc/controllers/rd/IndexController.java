package com.realdolmen.rdfleet.webmvc.controllers.rd;

import com.realdolmen.rdfleet.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by JSTAX29 on 11/11/2015.
 */
@Controller
@RequestMapping({"/", "/index"})
public class IndexController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(method = RequestMethod.GET)
    public String showIndex(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        for(GrantedAuthority authority : user.getAuthorities()){
            if(authority.getAuthority().equalsIgnoreCase("ROLE_FLEETEMPLOYEE")){
                model.addAttribute("employeeList", employeeService.findAllRdEmployeesInServiceWithPendingRequestOrNoCar());
            }else if (authority.getAuthority().equalsIgnoreCase("ROLE_RDEMPLOYEE")){
                model.addAttribute("employee", employeeService.findRdEmployeeByEmail(user.getUsername()));
            }
        }

        return "/index";
    }
}
