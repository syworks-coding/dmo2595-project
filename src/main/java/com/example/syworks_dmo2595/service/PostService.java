package com.example.syworks_dmo2595.service;

import com.example.syworks_dmo2595.service.dto.request.PostServiceSaveRequest;
import com.example.syworks_dmo2595.service.dto.response.PostServiceFindPostResponse;

public interface PostService {
    public void savePost(PostServiceSaveRequest request);
    public void deletePost(Long postId);
    public void changePost();
    public PostServiceFindPostResponse findPost(Long postId);
}
