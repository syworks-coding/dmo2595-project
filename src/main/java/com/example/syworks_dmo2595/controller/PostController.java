package com.example.syworks_dmo2595.controller;

import com.example.syworks_dmo2595.controller.dto.request.CommentSaveRequestBody;
import com.example.syworks_dmo2595.controller.dto.request.PostEditRequestBody;
import com.example.syworks_dmo2595.controller.dto.request.PostSaveRequestBody;
import com.example.syworks_dmo2595.service.PostService;
import com.example.syworks_dmo2595.service.dto.request.PostServiceEditRequest;
import com.example.syworks_dmo2595.service.dto.request.PostServiceSaveCommentRequest;
import com.example.syworks_dmo2595.service.dto.request.PostServiceSaveRequest;
import com.example.syworks_dmo2595.service.dto.response.PostServiceFindPostDetailResponse;
import com.example.syworks_dmo2595.service.dto.response.PostServiceLoadPostListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;


    @GetMapping("/board")
    public String loadBoardPage(Model model) {
        model.addAttribute("postList", postService.loadPostList());

        return "/board/board";
    }//페이지

    @GetMapping("/upload")
    public String loadPostUploadPage(Model model) {
        return "/board/upload";
    }//페이지

    @GetMapping("/detail/{postId}")
    public String loadPostDetailPage(Model model,
                                     @PathVariable Long postId, HttpServletRequest request, HttpServletResponse response) {
        PostServiceFindPostDetailResponse postServiceFindPostDetailResponse = postService.findPost(postId);
        model.addAttribute("userName", postServiceFindPostDetailResponse.getUserName());
        model.addAttribute("loginId", postServiceFindPostDetailResponse.getLoginId());
        model.addAttribute("title", postServiceFindPostDetailResponse.getTitle());
        model.addAttribute("content", postServiceFindPostDetailResponse.getContent());
        postService.updataViewCount(postId, response, request);

        model.addAttribute("commentList", postService.findComment(postId));




        return "/board/detail";
    }//페이지

//    @GetMapping("detail/comment/{postId}")
//    public String loadPostCommentPage(@PathVariable Long postId, Model model) {
//
//        return "/board/comment";
//    }//페이지

    @GetMapping("detail/edit/{postId}")
    public String loadPostEditPage(@PathVariable Long postId, Model model) {

        return "/board/edit";
    }//페이지

    //게시글 수정
    @PutMapping("detail/{postId}")
    public String editPost(@PathVariable Long postId, PostEditRequestBody postEditRequestBody, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            model.addAttribute("message", "로그인이 필요합니다");
            model.addAttribute("searchUrl", "/users/login/");

            return "message";
        }
        try {
            postService.editPost(PostServiceEditRequest.builder()
                    .title(postEditRequestBody.getTitle())
                    .content(postEditRequestBody.getContent())
                    .password(postEditRequestBody.getPassword())
                    .postId(postId)
                    .build());
            model.addAttribute("message", "게시글 수정 성공");
            model.addAttribute("searchUrl", "/posts/board");

            return "message";

        }catch (Exception e) {
            model.addAttribute("message", "게시글 수정 실패");
            model.addAttribute("searchUrl", "/posts/board");

            return "message";
        }

    }


    //게시글 업로드
    @PostMapping("/upload")
    public final String uploadPost(PostSaveRequestBody requestBody, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            model.addAttribute("message", "로그인이 필요합니다");
            model.addAttribute("searchUrl", "/users/login");

            return "message";
        }


        //TODO userId는 세션, 쿠키 등을 통해 가져오기
        postService.savePost(PostServiceSaveRequest.builder()
                .userId((Long) session.getAttribute("userId"))
                .title(requestBody.getTitle())
                .content(requestBody.getContent())
                .password(requestBody.getPassword())
                .build());

        return "redirect:/posts/board";
    }

    @PostMapping("detail/comment/{postId}")
    public String saveComment(@PathVariable Long postId, CommentSaveRequestBody requestBody, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("userId");
        postService.saveComment(PostServiceSaveCommentRequest.builder()
                        .postId(postId)
                        .userId(userId)
                        .comment(requestBody.getComment())
                .build());
        return "redirect:/posts/detail/" + postId;
    }

    @DeleteMapping("/detail/{postId}")
    public final String deletePostById(@PathVariable Long postId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute("userId");
        try {
            postService.deletePost(postId, userId);
            model.addAttribute("message", "게시글 삭제 성공");
            model.addAttribute("searchUrl", "/posts/board");
            return "message";
        }catch (Exception e){
            e.printStackTrace();
        }


        model.addAttribute("message", "게시글 삭제 실패");
        model.addAttribute("searchUrl", "/posts/board");

        return "message";
    }



}
