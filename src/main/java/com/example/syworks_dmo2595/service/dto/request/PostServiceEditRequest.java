package com.example.syworks_dmo2595.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class PostServiceEditRequest {
    private String title;
    private String content;
    private String password;
    private Long postId;
}
