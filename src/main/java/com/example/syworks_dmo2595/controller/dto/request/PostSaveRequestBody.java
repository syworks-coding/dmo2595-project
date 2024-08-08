package com.example.syworks_dmo2595.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostSaveRequestBody {
    private String userId;
    private String title;
    private String content;
    private String password;
}
