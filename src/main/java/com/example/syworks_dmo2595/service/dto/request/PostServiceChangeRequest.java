package com.example.syworks_dmo2595.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostServiceChangeRequest {
    private String content;
    private String title;
}
