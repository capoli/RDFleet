package com.realdolmen.rdfleet.webmvc.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by OCPAX79 on 1/11/2015.
 */
@Controller
public class AppController {
    @RequestMapping(value = "/app/**")
    public String redirectForApp() {
        return "forward:/";
    }

    @Secured("RdEmployee")
    @RequestMapping(value = "/app/rdemployee/**")
    public String redirectForRdEmployee() {
        return "forward:/";
    }

    @Secured("FleetEmployee")
    @RequestMapping(value = "/app/fleetemployee/**")
    public String redirectForFleetEmployee() {
        return "forward:/";
    }
}
