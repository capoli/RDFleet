package com.realdolmen.rdfleet.webmvc.controllers.fleet;

import com.realdolmen.rdfleet.domain.Car;
import com.realdolmen.rdfleet.domain.CarOption;
import com.realdolmen.rdfleet.service.CarOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMappingName;

/**
 * Created by JSTAX29 on 5/11/2015.
 */
@Controller
@RequestMapping("/fleet/options")
public class CarOptionManagementController {
    @Autowired
    private CarOptionService carOptionService;
    @Autowired
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET)
    public String viewCarOptions(Model model) {
        model.addAttribute("optionList", carOptionService.findAllCarOptions());
        return "fleet/caroption.list";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String goToEditPage(@PathVariable Long id, Model model) {
        model.addAttribute("carOption", carOptionService.findCarOptionById(id));
        return "fleet/caroption.detail";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createOption(Model model) {
        model.addAttribute("carOption", new CarOption());
        return "fleet/caroption.detail";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String editOption(@ModelAttribute CarOption carOption, BindingResult errors, Model model) {
        validator.validate(carOption, errors);
        if (errors.hasErrors()) {
            model.addAttribute(carOptionService.findAllCarOptions());
            return "fleet/caroption.detail";
        }

        carOptionService.createCarOption(carOption);
        return "redirect:" + fromMappingName("COMC#viewCarOptions").build();
    }
}
