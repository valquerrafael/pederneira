package br.edu.ifpb.pweb2.pederneira.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView showHome(ModelAndView model) {
        model.setViewName("home");
        return model;
    }

    @GetMapping("/home")
    public ModelAndView redirectHome(ModelAndView model) {
        model.setViewName("redirect:/");
        return model;
    }
}
