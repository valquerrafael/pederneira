package br.edu.ifpb.pweb2.pederneira.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

public class AuthController {
    @GetMapping(value = "/auth")
    public ModelAndView showLogin(ModelAndView mav) {
        mav.setViewName("auth");
        return mav;
    }
}
