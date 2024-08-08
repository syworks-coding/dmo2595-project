package com.example.syworks_dmo2595.common.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;


public class NeedLoginResponse {

    public ModelAndView needLogin(Model model) {

        ModelAndView modelAndView =  new ModelAndView();


        model.addAttribute("message", "로그인이 필요합니다");
        model.addAttribute("searchUrl", "/users/login");
        modelAndView.setViewName("message");

        return modelAndView;


    }
}
