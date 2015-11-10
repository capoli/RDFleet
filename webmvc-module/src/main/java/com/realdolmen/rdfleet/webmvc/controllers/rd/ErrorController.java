package com.realdolmen.rdfleet.webmvc.controllers.rd;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by OCPAX79 on 9/11/2015.
 */
@Controller
@RequestMapping("/error")
public class ErrorController {
    @RequestMapping(method = RequestMethod.GET)
    public String showErrorPage(Exception e) {
        return "error";
    }
}
