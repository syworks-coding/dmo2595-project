package com.example.syworks_dmo2595.service.dto.response;

import com.example.syworks_dmo2595.repository.Comment;
import com.example.syworks_dmo2595.vos.CommentVO;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class PostServiceFindCommentResponse {
    private List<CommentVO> commentList;
    private List<CommentVO> replyList;

}
