package com.example.syworks_dmo2595.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @GetMapping()
    public String mainPage() {
        return "index";
    }

    @GetMapping("/chat")
    public ModelAndView chatPage(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        ModelAndView modelAndView =  new ModelAndView();

        if (session == null) {
            model.addAttribute("userName", "익명");
        }
        else {
            model.addAttribute("userName", session.getAttribute("userName"));
        }
        return modelAndView;
    }
}
