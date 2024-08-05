package com.example.syworks_dmo2595.service;

import com.example.syworks_dmo2595.repository.Post;
import com.example.syworks_dmo2595.service.dto.request.PostServiceEditRequest;
import com.example.syworks_dmo2595.service.dto.request.PostServiceSaveCommentRequest;
import com.example.syworks_dmo2595.service.dto.request.PostServiceSaveRequest;
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
    public PostServiceFindPostDetailResponse findPost(Long postId);
    public List<PostServiceLoadPostListResponse> loadPostList();
    public List<PostServiceFindCommentResponse> findComment(Long postId);
    public void saveComment(PostServiceSaveCommentRequest request);
    public void editPost(PostServiceEditRequest request);
    public void updataViewCount(Long postId, HttpServletResponse response, HttpServletRequest request);

}
