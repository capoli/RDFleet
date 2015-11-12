package com.realdolmen.rdfleet.webmvc.controllers.rd;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/error")
public class ErrorController {
    @RequestMapping(method = RequestMethod.GET)
    public String showErrorPage(Exception e) {
        return "error";
    }
}
