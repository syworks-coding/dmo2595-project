package com.example.syworks_dmo2595.vos;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentVO {
    private Long commentId;
    private String userName;
    private Long parentId;
    private String comment;
    private Integer likeCount;
}
