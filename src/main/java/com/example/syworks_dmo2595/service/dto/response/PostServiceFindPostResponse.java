package com.example.syworks_dmo2595.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class PostServiceFindPostResponse {
    private String userId;
    private String title;
    private String content;
}
