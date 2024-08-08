package com.example.syworks_dmo2595.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UserLoginRequestBody {
    private String loginId;
    private String password;
}
