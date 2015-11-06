package com.realdolmen.rdfleet.webmvc.controllers.fleet;

import com.realdolmen.rdfleet.domain.Pack;
import com.realdolmen.rdfleet.service.PackService;
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
 * Created by JSTAX29 on 6/11/2015.
 */
@Controller
@RequestMapping("/fleet/packs")
public class PackManagementController {
    @Autowired
    private PackService packService;
    @Autowired
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET)
    public String viewPacks(Model model) {
        model.addAttribute("packList", packService.findAllPacks());
        return "fleet/pack.list";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String goToEditPage(@PathVariable Long id, Model model) {
        model.addAttribute("pack", packService.findPackById(id));
        return "fleet/pack.detail";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createPack(Model model) {
        model.addAttribute("pack", new Pack());
        return "fleet/pack.detail";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String editPack(@ModelAttribute Pack pack, BindingResult errors, Model model) {
        validator.validate(pack, errors);
        if (errors.hasErrors()) {
            model.addAttribute(packService.findAllPacks());
            return "fleet/pack.detail";
        }

        packService.createPack(pack);
        return "redirect:" + fromMappingName("PMC#viewPacks").build();
    }
}
