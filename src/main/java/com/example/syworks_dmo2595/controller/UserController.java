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
    public String loadSignupPage(Model model) {
        model.addAttribute("name", "내이름");
        model.addAttribute("img", "image/파일명");
        return "signup";
    }
    @GetMapping("/logout")
    public String logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
    @GetMapping("/login")
    public String loginUser() {

        return "login";
    }

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
    public final String loginUser(UserLoginRequestBody requestBody, HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if(session == null) {
//            userService.loginUser(UserServiceLoginRequest.builder()
//                    .loginId(requestBody.getLoginId())
//                    .password(requestBody.getPassword())
//                    .build());
//
//            session.setAttribute("loginId", requestBody.getLoginId());
//
//        }
        userService.loginUser(UserServiceLoginRequest.builder()
                .loginId(requestBody.getLoginId())
                .password(requestBody.getPassword())
                .build());


        return "redirect:/";
    }


}
