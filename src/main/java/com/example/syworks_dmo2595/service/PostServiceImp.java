package com.example.syworks_dmo2595.service;

import com.example.syworks_dmo2595.repository.*;
import com.example.syworks_dmo2595.service.dto.request.*;
import com.example.syworks_dmo2595.service.dto.response.PostServiceDeleteCommentResponse;
import com.example.syworks_dmo2595.service.dto.response.PostServiceFindCommentResponse;
import com.example.syworks_dmo2595.service.dto.response.PostServiceFindPostDetailResponse;
import com.example.syworks_dmo2595.service.dto.response.PostServiceLoadPostListResponse;
import com.example.syworks_dmo2595.vos.CommentVO;
import com.example.syworks_dmo2595.vos.ReplyVO;
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
    public Long findUserByCommentId(Long commentId) {

        return commentRepository.findByCommentId(commentId).getUserId();
    }

    @Override
    public Long findPostIdByCommentId(Long commentId) {

        return commentRepository.findByCommentId(commentId).getPostId();
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
                .viewCount(post.getViewCount())
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
    public PostServiceFindCommentResponse findCommentList(Long postId) {
        List<Comment> commentList = commentRepository.findAllByPostIdAndParentIdIsNullOrderByCommentId(postId);
        List<Comment> replyList = commentRepository.findAllByParentIdIsNotNullAndPostIdOrderByCommentId(postId);
//        List<Comment> commentList = commentRepository.findAllByPostIdOrderByCommentId(postId);
        PostServiceFindCommentResponse responseLists = new PostServiceFindCommentResponse();
        List<CommentVO> responseCommentList = new ArrayList<>();
        List<ReplyVO> responseReplyList = new ArrayList<>();

        Integer likeCount = 0;
        for (Comment comment : commentList) {
            if (comment.getParentId() == null) {
                User user = userRepository.findByUserId(comment.getUserId());
                likeCount = likeRepository.countByCommentId(comment.getCommentId());


                CommentVO commentVO = new CommentVO();
                commentVO.setCommentId(comment.getCommentId());
                commentVO.setUserName(user.getUserName() + '[' + user.getLoginId() + ']');
                commentVO.setComment(comment.getComment());
                commentVO.setParentId(comment.getParentId());
                commentVO.setLikeCount(likeCount);

                responseCommentList.add(commentVO);


            }
        }

        for (Comment reply : replyList) {
            if (reply.getParentId() != null) {
                User user = userRepository.findByUserId(reply.getUserId());
                likeCount = likeRepository.countByCommentId(reply.getCommentId());

                ReplyVO replyVO = new ReplyVO();

                replyVO.setCommentId(reply.getCommentId());
                replyVO.setUserName(user.getUserName() + '[' + user.getLoginId() + ']');
                replyVO.setComment(reply.getComment());
                replyVO.setParentId(reply.getParentId());
                replyVO.setLikeCount(likeCount);

                responseReplyList.add(replyVO);

            }

        }
        responseLists.setCommentList(responseCommentList);
        responseLists.setReplyList(responseReplyList);
        return responseLists;
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
    public void editPost(PostServiceEditPostRequest request) {
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
    public PostServiceDeleteCommentResponse deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findByCommentId(commentId);

        if (comment.getUserId().equals(userId)) {
            commentRepository.delete(comment);
        } else {
            return PostServiceDeleteCommentResponse.builder()
                    .success(false)
                    .postId(comment.getPostId())
                    .build();
        }

        return PostServiceDeleteCommentResponse.builder()
                .success(true)
                .postId(comment.getPostId())
                .build();
    }

    @Override
    public void editComment(PostServiceEditCommentRequest request) {
        Comment comment = commentRepository.findByCommentId(request.getCommentId());
        comment.setComment(request.getComment());
        commentRepository.save(comment);
    }

    @Override
    public Long saveReply(PostServiceSaveReplyRequest request) {

        Comment parentComment = commentRepository.findByCommentId(request.getParentId());

        commentRepository.save(Comment.builder()
                .userId(request.getUserId())
                .parentId(request.getParentId())
                .postId(parentComment.getPostId())
                .comment(request.getReply())
                .build());

        return parentComment.getPostId();

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
