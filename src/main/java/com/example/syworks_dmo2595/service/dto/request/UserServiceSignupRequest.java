package com.example.syworks_dmo2595.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserServiceSignupRequest {
    private String loginId;
    private String password;
    private String userName;
}
