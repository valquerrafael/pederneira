package br.edu.ifpb.pweb2.pederneira.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping(value = {"/", "/home"})
    public ModelAndView showHome(ModelAndView mav) {
        mav.setViewName("home");
        return mav;
    }
}
