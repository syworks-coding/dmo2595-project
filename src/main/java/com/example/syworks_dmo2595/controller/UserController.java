package com.example.syworks_dmo2595.controller;

import com.example.syworks_dmo2595.controller.dto.request.UserLoginRequestBody;
import com.example.syworks_dmo2595.controller.dto.request.UserSignUpRequestBody;
import com.example.syworks_dmo2595.service.UserService;
import com.example.syworks_dmo2595.service.dto.request.UserServiceLoginRequest;
import com.example.syworks_dmo2595.service.dto.request.UserServiceSignupRequest;
import com.example.syworks_dmo2595.service.dto.response.UserServiceLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/users")
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //페이지 불러오기
    @GetMapping("/signup")
    public String loadSignupPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            model.addAttribute("message", "로그아웃 해주세요");
            model.addAttribute("searchUrl", "/");
            return "message";
        }
        return "signup";
    }
    @GetMapping("/logout")
    public String logoutUser(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            System.out.println("세션파기 : " + session.getAttribute("userName"));
            session.invalidate();
            model.addAttribute("message", "로그아웃 성공");
            model.addAttribute("searchUrl", "/");
        }
        else {
            model.addAttribute("message", "로그인 되어있지 않습니다.");
            model.addAttribute("searchUrl", "/");

        }

        return "message";
    }
    @GetMapping("/login")
    public String loadLoginPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            model.addAttribute("message", "이미 로그인 상태입니다.");
            model.addAttribute("searchUrl", "/");
            return "message";
        }
        return "login";
    }//페이지

    @PostMapping("/signup")
    public final String saveUser(UserSignUpRequestBody requestBody) {

        userService.signupUser(UserServiceSignupRequest.builder()
                        .loginId(requestBody.getLoginId())
                        .userName(requestBody.getUserName())
                        .password(requestBody.getPassword())
                .build());

        return "redirect:/";

    }

    @PostMapping("/login")
    public final String loginUser(UserLoginRequestBody requestBody, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        UserServiceLoginResponse userServiceLoginResponse;
        if(session == null) {
            session = request.getSession(true);
            session.setMaxInactiveInterval(1800);
            try{
                userServiceLoginResponse = userService.loginUser(UserServiceLoginRequest.builder()
                        .loginId(requestBody.getLoginId())
                        .password(requestBody.getPassword())
                        .build());
            } catch (Exception e) {
                session.invalidate();
                model.addAttribute("message", "아이디 비밀번호 불일치");
                model.addAttribute("searchUrl", "/");
                return "message";
            }

            session.setAttribute("userId", userServiceLoginResponse.getUserId());
            model.addAttribute("message", "로그인 성공");
            model.addAttribute("searchUrl", "/");

            return "message";
        }
        else{
            model.addAttribute("message", "오류");
            model.addAttribute("searchUrl", "/");
            return "message";
        }


//        userService.loginUser(UserServiceLoginRequest.builder()
//                .loginId(requestBody.getLoginId())
//                .password(requestBody.getPassword())
//                .build());
    }


}
