package com.example.syworks_dmo2595.service;

import com.example.syworks_dmo2595.service.dto.request.UserServiceLoginRequest;
import com.example.syworks_dmo2595.service.dto.request.UserServiceSignupRequest;
import com.example.syworks_dmo2595.service.dto.response.UserServiceLoginResponse;

public interface UserService {

    public UserServiceLoginResponse loginUser(UserServiceLoginRequest request);
    public void signupUser(UserServiceSignupRequest request);
}
