package com.example.syworks_dmo2595.service;

import com.example.syworks_dmo2595.repository.User;
import com.example.syworks_dmo2595.service.dto.request.*;
import com.example.syworks_dmo2595.service.dto.response.PostServiceDeleteCommentResponse;
import com.example.syworks_dmo2595.service.dto.response.PostServiceFindCommentResponse;
import com.example.syworks_dmo2595.service.dto.response.PostServiceFindPostDetailResponse;
import com.example.syworks_dmo2595.service.dto.response.PostServiceLoadPostListResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PostService {
    public void savePost(PostServiceSaveRequest request);
    public void deletePost(Long postId, Long userId);
    public void changePost();
    public Long findUserByCommentId(Long commentId);
    public Long findPostIdByCommentId(Long commentId);
    public PostServiceFindPostDetailResponse findPost(Long postId);
    public List<PostServiceLoadPostListResponse> loadPostList();
    public PostServiceFindCommentResponse findCommentList(Long postId);
    public void saveComment(PostServiceSaveCommentRequest request);
    public void editPost(PostServiceEditPostRequest request);
    public void updataViewCount(Long postId, HttpServletResponse response, HttpServletRequest request);
    public Boolean updatePostLike(Long postId, Long userId);
    public Long updateCommentLike(Long commentId, Long userId);
    public PostServiceDeleteCommentResponse deleteComment(Long commentId, Long userId);
    public void editComment(PostServiceEditCommentRequest request);
    public Long saveReply(PostServiceSaveReplyRequest request);

}
