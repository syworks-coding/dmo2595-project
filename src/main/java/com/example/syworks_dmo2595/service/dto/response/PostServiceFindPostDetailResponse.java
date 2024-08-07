package com.example.syworks_dmo2595.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class PostServiceFindPostDetailResponse {
    private Long userId;
    private String loginId;
    private String userName;
    private String title;
    private String content;
    private Long viewCount;
}
