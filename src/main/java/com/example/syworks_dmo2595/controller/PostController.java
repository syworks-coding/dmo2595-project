package com.example.syworks_dmo2595.controller;

import com.example.syworks_dmo2595.controller.dto.request.PostSaveRequestBody;
import com.example.syworks_dmo2595.service.PostService;
import com.example.syworks_dmo2595.service.dto.request.PostServiceSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;


    @GetMapping("/list")
    public String loadBoardPage() {
        return "board";
    }

    @PostMapping()
    public final String uploadPost(@RequestBody PostSaveRequestBody requestBody) {
        postService.savePost(PostServiceSaveRequest.builder()
                .userId(requestBody.getUserId())
                .title(requestBody.getTitle())
                .content(requestBody.getContent())
                .password(requestBody.getPassword())
                .build());

        return "게시물 업로드 성공";
    }

    @DeleteMapping
    public final void deletePostById() {

    }
    @GetMapping
    public final void getPostById() {

    }
    @PutMapping
    public final void putPostById() {

    }

}
