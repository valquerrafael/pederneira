package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Institution;
import br.edu.ifpb.pweb2.pederneira.service.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/institution")
public class InstitutionController {

    @Autowired
    private InstitutionService service;

    @GetMapping("/register")
    public String getRegisterForm(Institution institution, Model model) {
        model.addAttribute("institution", institution);
        return "institution/register";
    }

    @PostMapping("/register")
    public String register(
        Institution institution,
        BindingResult result,
        RedirectAttributes attributes
    ) {
        if (result.hasErrors())
            return "home";

        this.service.register(institution);

        attributes.addFlashAttribute(
            "message",
            institution.getName() + institution.getAcronym() + institution.getPhone()
        );
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String readOne(@PathVariable(value = "id") Integer id, Model model) {
        model.addAttribute("institution", this.service.readOne(id));
        return "home";
    }

    @PostMapping("/{id}")
    public String update(
        @PathVariable(value = "id") Integer id,
        Institution institution,
        RedirectAttributes attributes
    ) {
        this.service.update(id, institution);

        attributes.addFlashAttribute(
            "message",
            "Institution updated successfully"
        );
        return "home";
    }

    @DeleteMapping("/{id}")
    public String delete(
        @PathVariable(value = "id") Integer id,
        RedirectAttributes attributes
    ) {
        this.service.delete(id);

        attributes.addFlashAttribute(
            "message",
            "Institution deleted successfully"
        );
        return "home";
    }

}
