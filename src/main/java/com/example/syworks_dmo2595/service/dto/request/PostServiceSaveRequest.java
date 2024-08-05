package com.example.syworks_dmo2595.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class PostServiceSaveRequest {
    private Long userId;
    private String title;
    private String content;
    private String password;
}
