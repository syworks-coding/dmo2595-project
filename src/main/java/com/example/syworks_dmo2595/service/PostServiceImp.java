package com.example.syworks_dmo2595.service;

import com.example.syworks_dmo2595.repository.Post;
import com.example.syworks_dmo2595.repository.PostRepository;
import com.example.syworks_dmo2595.service.dto.request.PostServiceSaveRequest;
import com.example.syworks_dmo2595.service.dto.response.PostServiceFindPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {

    private final PostRepository postRepository;

    @Override
    public void savePost(PostServiceSaveRequest request) {
        postRepository.save(Post.builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .password(request.getPassword())
                        .userId(request.getUserId())
                .build());
    }

    @Override
    public void deletePost(Long postId) {
        try {
            postRepository.deleteById(postId);
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }

    @Override
    public void changePost() {

    }

    @Override
    public PostServiceFindPostResponse findPost(Long postId) {
        Post post = postRepository.findById(postId).get();

        return PostServiceFindPostResponse.builder()
                .userId(post.getUserId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }
}
