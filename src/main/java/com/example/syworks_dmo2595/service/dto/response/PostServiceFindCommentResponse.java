package com.example.syworks_dmo2595.service.dto.response;

import lombok.*;

@Getter
@Builder
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class PostServiceFindCommentResponse {
    private Long commentId;
    private String userName;
    private Long parentId;
    private String comment;
    private Integer likeCount;
}
