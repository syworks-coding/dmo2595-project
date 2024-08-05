package com.example.syworks_dmo2595.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PostServiceSaveCommentRequest {
    private String comment;
    private Long userId;
    private Long parentId;
    private Long postId;
}
