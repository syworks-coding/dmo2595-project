package com.example.syworks_dmo2595.controller;

import com.example.syworks_dmo2595.common.response.NeedLoginResponse;
import com.example.syworks_dmo2595.controller.dto.request.CommentSaveRequestBody;
import com.example.syworks_dmo2595.controller.dto.request.PostEditRequestBody;
import com.example.syworks_dmo2595.controller.dto.request.PostSaveRequestBody;
import com.example.syworks_dmo2595.repository.User;
import com.example.syworks_dmo2595.service.PostService;
import com.example.syworks_dmo2595.service.dto.request.*;
import com.example.syworks_dmo2595.service.dto.response.PostServiceDeleteCommentResponse;
import com.example.syworks_dmo2595.service.dto.response.PostServiceFindCommentResponse;
import com.example.syworks_dmo2595.service.dto.response.PostServiceFindPostDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private ModelAndView modelAndView = new ModelAndView();
    private NeedLoginResponse needLoginResponse = new NeedLoginResponse();



    @GetMapping("/board")
    public ModelAndView loadBoardPage(Model model) {
        model.addAttribute("postList", postService.loadPostList());

        modelAndView.setViewName("board/board");
        return modelAndView;
    }//페이지

    @GetMapping("/upload")
    public ModelAndView loadPostUploadPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            modelAndView = needLoginResponse.needLogin(model);
        }
        else {
            modelAndView.setViewName("board/upload.html");
        }
        return modelAndView;
    }//페이지

    @GetMapping("/detail/{postId}")
    public ModelAndView loadPostDetailPage(Model model,
                                     @PathVariable Long postId, HttpServletRequest request, HttpServletResponse response) {

        postService.updataViewCount(postId, response, request);
        PostServiceFindPostDetailResponse postServiceFindPostDetailResponse = postService.findPost(postId);
        model.addAttribute("userName", postServiceFindPostDetailResponse.getUserName());
        model.addAttribute("loginId", postServiceFindPostDetailResponse.getLoginId());
        model.addAttribute("title", postServiceFindPostDetailResponse.getTitle());
        model.addAttribute("content", postServiceFindPostDetailResponse.getContent());
        model.addAttribute("viewCount", postServiceFindPostDetailResponse.getViewCount());

        PostServiceFindCommentResponse postServiceFindCommentResponse = postService.findCommentList(postId);
        model.addAttribute("commentList", postServiceFindCommentResponse.getCommentList());
        model.addAttribute("replyList", postServiceFindCommentResponse.getReplyList());



        modelAndView.setViewName("board/detail.html");

        return modelAndView;
    }//페이지

//    @GetMapping("detail/comment/{postId}")
//    public String loadPostCommentPage(@PathVariable Long postId, Model model) {
//
//        return "/board/comment";
//    }//페이지

    @GetMapping("detail/edit/{postId}")
    public ModelAndView loadPostEditPage(@PathVariable Long postId, Model model) {
        modelAndView.setViewName("board/edit.html");

        return modelAndView;
    }//페이지


    @GetMapping("/detail/edit/comment/{commentId}")
    public ModelAndView loadCommentEditPage(@PathVariable Long commentId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            modelAndView = needLoginResponse.needLogin(model);
            return modelAndView;
        }

        Long postId = postService.findPostIdByCommentId(commentId);
        Long userId = (Long) session.getAttribute("userId");

        if (!userId.equals(postService.findUserByCommentId(commentId))) {
            model.addAttribute("message", "본인 댓글만 삭제가 가능합니다");
            model.addAttribute("searchUrl", "/posts/detail/" + postId);
            modelAndView.setViewName("message.html");
            return modelAndView;
        }

        model.addAttribute("commmentId", commentId);
        modelAndView.setViewName("board/edit-comment.html");
        return modelAndView;
    }//페이지

    @GetMapping("detail/reply/{commentId}")
    public ModelAndView loadSaveReplyPage(@PathVariable Long commentId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            modelAndView = needLoginResponse.needLogin(model);
            return modelAndView;
        }

        model.addAttribute("commnetId", commentId);
        modelAndView.setViewName("board/reply.html");
        return modelAndView;
    }



    //게시글 수정
    @PutMapping("detail/{postId}")
    public ModelAndView editPost(@PathVariable Long postId, PostEditRequestBody postEditRequestBody, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            modelAndView = needLoginResponse.needLogin(model);
            return modelAndView;
        }
        try {
            postService.editPost(PostServiceEditPostRequest.builder()
                    .title(postEditRequestBody.getTitle())
                    .content(postEditRequestBody.getContent())
                    .password(postEditRequestBody.getPassword())
                    .postId(postId)
                    .build());
            model.addAttribute("message", "게시글 수정 성공");
            model.addAttribute("searchUrl", "/posts/board");
            modelAndView.setViewName("message.html");

            return modelAndView;

        }catch (Exception e) {
            model.addAttribute("message", "게시글 수정 실패");
            model.addAttribute("searchUrl", "/posts/board");
            modelAndView.setViewName("message.html");

            return modelAndView;
        }

    }


    //게시글 업로드
    @PostMapping("/upload")
    public final ModelAndView uploadPost(PostSaveRequestBody requestBody, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            modelAndView = needLoginResponse.needLogin(model);

            return modelAndView;
        }


        postService.savePost(PostServiceSaveRequest.builder()
                .userId((Long) session.getAttribute("userId"))
                .title(requestBody.getTitle())
                .content(requestBody.getContent())
                .password(requestBody.getPassword())
                .build());
        modelAndView.setViewName("redirect:/posts/board");

        return modelAndView;
    }

    //댓글 저장
    @PostMapping("detail/comment/{postId}")
    public ModelAndView saveComment(@PathVariable Long postId, CommentSaveRequestBody requestBody, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null) {

            modelAndView = needLoginResponse.needLogin(model);

            return modelAndView;
        }


        Long userId = (Long) session.getAttribute("userId");

        postService.saveComment(PostServiceSaveCommentRequest.builder()
                        .postId(postId)
                        .userId(userId)
                        .comment(requestBody.getComment())
                .build());
        modelAndView.setViewName("redirect:/posts/detail/" + postId);

        return modelAndView;
    }

    //게시글 삭제
    @DeleteMapping("/detail/{postId}")
    public final ModelAndView deletePostById(@PathVariable Long postId, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            modelAndView = needLoginResponse.needLogin(model);
            return modelAndView;
        }

        Long userId = (Long) session.getAttribute("userId");
        try {
            postService.deletePost(postId, userId);
            model.addAttribute("message", "게시글 삭제 성공");
            model.addAttribute("searchUrl", "/posts/board");
            modelAndView.setViewName("message.html");

            return modelAndView;
        }catch (Exception e){
            e.printStackTrace();
        }


        model.addAttribute("message", "게시글 삭제 실패");
        model.addAttribute("searchUrl", "/posts/board");
        modelAndView.setViewName("message.html");

        return modelAndView;
    }

    //좋아요 추가, 삭제
    @PutMapping("/detail/like/post/{postId}")
    public final ModelAndView updataPostLike(@PathVariable Long postId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {

            modelAndView = needLoginResponse.needLogin(model);

            return modelAndView;
        }


        Long userId = (Long) session.getAttribute("userId");

        postService.updatePostLike(postId, userId);
        modelAndView.setViewName("redirect:/posts/detail/" + postId);

        return modelAndView;
    }
    @PutMapping("/detail/like/comment/{commentId}")
    public final ModelAndView updateCommentLike(@PathVariable Long commentId, Model model, HttpServletRequest request) {


        HttpSession session = request.getSession(false);

        if (session == null) {

            modelAndView = needLoginResponse.needLogin(model);

            return modelAndView;
        }

        Long userId = (Long) session.getAttribute("userId");

        Long postId = postService.updateCommentLike(commentId, userId);
        modelAndView.setViewName("redirect:/posts/detail/" + postId);

        return modelAndView;
    }

    @DeleteMapping("/detail/comment")
    public final ModelAndView deleteComment(Long commentId, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        if (session == null) {

            modelAndView = needLoginResponse.needLogin(model);

            return modelAndView;
        }
        Long userId = (Long) session.getAttribute("userId");


        PostServiceDeleteCommentResponse postServiceDeleteCommentResponse = postService.deleteComment(commentId, userId);

        if (!postServiceDeleteCommentResponse.isSuccess()) {
            model.addAttribute("message", "본인 댓글만 삭제가 가능합니다.");
        }
        else {
            model.addAttribute("message", "댓글 삭제 성공");
        }

        model.addAttribute("searchUrl", "/posts/detail/" + postServiceDeleteCommentResponse.getPostId());
        modelAndView.setViewName("message.html");

        return modelAndView;

    }

    @PutMapping("/detail/comment/{commentId}")
    public final ModelAndView editComment(@PathVariable Long commentId, String content, HttpServletRequest request, Model model) {



        Long postId = postService.findPostIdByCommentId(commentId);

        postService.editComment(PostServiceEditCommentRequest.builder()
                .comment(content)
                .commentId(commentId)
                .build());

        model.addAttribute("message", "댓글 수정 성공.");
        model.addAttribute("searchUrl", "/posts/detail/" + postId);
        modelAndView.setViewName("message.html");

        return modelAndView;
    }

    @PostMapping("/detail/reply/{parentId}")
    public final ModelAndView saveReply(@PathVariable Long parentId, String reply, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);

        Long userId = (Long) session.getAttribute("userId");
        Long postId = postService.saveReply(PostServiceSaveReplyRequest.builder()
                .reply(reply)
                .parentId(parentId)
                .userId(userId)
                .build());

        model.addAttribute("postId", postId);
        modelAndView.setViewName("redirect:/posts/detail/" + postId);


        return modelAndView;
    }



}
