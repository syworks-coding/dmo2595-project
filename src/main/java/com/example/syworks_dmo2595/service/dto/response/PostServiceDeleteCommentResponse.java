package com.example.syworks_dmo2595.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostServiceDeleteCommentResponse {
    private boolean success;
    private Long postId;

}
