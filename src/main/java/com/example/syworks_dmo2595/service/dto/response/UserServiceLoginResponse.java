package com.example.syworks_dmo2595.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@AllArgsConstructor
public class UserServiceLoginResponse {
    private String userName;
    private String loginId;

}
