package com.example.syworks_dmo2595.service;

import com.example.syworks_dmo2595.repository.*;
import com.example.syworks_dmo2595.service.dto.request.PostServiceEditRequest;
import com.example.syworks_dmo2595.service.dto.request.PostServiceSaveCommentRequest;
import com.example.syworks_dmo2595.service.dto.request.PostServiceSaveRequest;
import com.example.syworks_dmo2595.service.dto.response.PostServiceFindCommentResponse;
import com.example.syworks_dmo2595.service.dto.response.PostServiceFindPostDetailResponse;
import com.example.syworks_dmo2595.service.dto.response.PostServiceLoadPostListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Override
    public void savePost(PostServiceSaveRequest request) {
        postRepository.save(Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .password(request.getPassword())
                .userId(request.getUserId())
                .viewCount(0L)
                .build());
    }

    @Override
    public void deletePost(Long postId, Long userId) {

        Post post = postRepository.findByPostId(postId);
        if (post.getUserId().equals(userId)) {
            postRepository.delete(post);
        } else {
            throw new RuntimeException("본인 아님");
        }
    }


    

    @Override
    public void changePost() {

    }

    @Override
    public PostServiceFindPostDetailResponse findPost(Long postId) {
        Post post = postRepository.findById(postId).get();
        User user = userRepository.findByUserId(post.getUserId());

        return PostServiceFindPostDetailResponse.builder()
                .userId(post.getUserId())
                .userName(user.getUserName())
                .loginId(user.getLoginId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    @Override
    public List<PostServiceLoadPostListResponse> loadPostList() {
        List<Post> postList = postRepository.findAllByOrderByPostIdAsc();
        List<PostServiceLoadPostListResponse> responseList = new ArrayList<>();
        for (Post post : postList) {
            User user = userRepository.findByUserId(post.getUserId());
            Long likeCount = likeRepository.countByPostId(post.getPostId());

            PostServiceLoadPostListResponse postServiceLoadPostListResponse = new PostServiceLoadPostListResponse();
            postServiceLoadPostListResponse.setUserName(user.getUserName());
            postServiceLoadPostListResponse.setTitle(post.getTitle());
            postServiceLoadPostListResponse.setViewCount(post.getViewCount());
            postServiceLoadPostListResponse.setPostId(post.getPostId());
            postServiceLoadPostListResponse.setLikeCount(likeCount);


            responseList.add(postServiceLoadPostListResponse);
        }
        return responseList;
    }

    @Override
    public List<PostServiceFindCommentResponse> findComment(Long postId) {
        List<Comment> commentList = commentRepository.findAllByPostIdOrderByCommentId(postId);
        List<PostServiceFindCommentResponse> responseList = new ArrayList<>();
        Integer likeCount = 0;
        for (Comment comment : commentList) {
            User user = userRepository.findByUserId(comment.getUserId());
            likeCount = likeRepository.countByCommentId(comment.getCommentId());
            PostServiceFindCommentResponse postServiceFindCommentResponse = new PostServiceFindCommentResponse();
            postServiceFindCommentResponse.setCommentId(comment.getCommentId());
            postServiceFindCommentResponse.setUserName(user.getUserName()+'['+user.getLoginId()+']');
            postServiceFindCommentResponse.setComment(comment.getComment());
            postServiceFindCommentResponse.setParentId(comment.getParentId());
            postServiceFindCommentResponse.setLikeCount(likeCount);

            responseList.add(postServiceFindCommentResponse);
        }
        return responseList;
    }

    @Override
    public void saveComment(PostServiceSaveCommentRequest request) {
        commentRepository.save(Comment.builder()
                        .userId(request.getUserId())
                        .postId(request.getPostId())
                        .parentId(request.getParentId())
                        .comment(request.getComment())

                .build());
    }

    @Override
    public void editPost(PostServiceEditRequest request) {
        Post post = postRepository.findByPostId(request.getPostId());
        if (post.getPassword().equals(request.getPassword())){
            post.setTitle(request.getTitle());
            post.setContent(request.getContent());
            postRepository.save(post);
        }
        else {
            throw new RuntimeException("비밀번호 불일치");
        }

    }

    @Override
    public void updataViewCount(Long postId, HttpServletResponse response, HttpServletRequest request) {
        Post post = postRepository.findByPostId(postId);
        Cookie[] cookies = request.getCookies();
        boolean checkCookie = false;
        int result = 0;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // 이미 조회를 한 경우 체크
                if (cookie.getName().equals("alreadyViewed" + postId)) checkCookie = true;

            }
            if (!checkCookie) {
                Cookie newCookie = createCookieForForNotOverlap(postId);
                response.addCookie(newCookie);
                post.setViewCount(post.getViewCount() + 1L);
                postRepository.save(post);
            }
        } else {
            Cookie newCookie = createCookieForForNotOverlap(postId);
            response.addCookie(newCookie);
            post.setViewCount(post.getViewCount() + 1L);
            postRepository.save(post);
        }
    }

    @Override
    public Boolean updatePostLike(Long postId, Long userId) {
        Boolean isExistLike = likeRepository.existsByPostIdAndUserId(postId, userId);
        if (isExistLike) {
            Like like = likeRepository.findByPostIdAndUserId(postId, userId);
            likeRepository.delete(like);
            return false;
        }
        else {
            Like like = Like.builder()
                    .postId(postId)
                    .userId(userId)
                    .build();
            likeRepository.save(like);

            return true;
        }


    }

    @Override
    public Long updateCommentLike(Long commentId, Long userId) {

        Boolean isExistLike = likeRepository.existsByCommentIdAndUserId(commentId, userId);
        Comment comment = commentRepository.findByCommentId(commentId);


        if (isExistLike) {
            Like like = likeRepository.findByCommentIdAndUserId(commentId, userId);
            likeRepository.delete(like);
        }
        else {
            Like like = Like.builder()
                    .commentId(commentId)
                    .userId(userId)
                    .build();
            likeRepository.save(like);

        }

        return comment.getPostId();
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findByCommentId(commentId);

        if (comment.getUserId().equals(userId)) {
            commentRepository.delete(comment);
        } else {
            throw new RuntimeException("본인 아님");
        }

    }


    private Cookie createCookieForForNotOverlap(Long postId) {
        Cookie cookie = new Cookie("alreadyViewed"+postId, String.valueOf(postId));
        cookie.setComment("조회수 중복 증가 방지 쿠키");
        cookie.setMaxAge(getRemainSecondForTommorow());
//        cookie.setMaxAge(1);
//        cookie.setHttpOnly(true);
        return cookie;
    }
    private int getRemainSecondForTommorow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tommorow = LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.DAYS);
        return (int) now.until(tommorow, ChronoUnit.SECONDS);
    }

}
