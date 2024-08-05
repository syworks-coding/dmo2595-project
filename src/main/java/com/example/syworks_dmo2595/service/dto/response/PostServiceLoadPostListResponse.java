package com.example.syworks_dmo2595.service.dto.response;

import com.example.syworks_dmo2595.repository.Post;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PostServiceLoadPostListResponse {
    private String userName;
    private Long postId;
    private String title;
    private Long viewCount;

}
