package br.edu.ifpb.pweb2.pederneira.controller;

import br.edu.ifpb.pweb2.pederneira.model.Institution;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/institution")
public class InstituionController {

    @GetMapping
    public String getRegisterForm(Institution institution, Model model) {
        model.addAttribute("institution", institution);
        return "institution/register";
    }

}
