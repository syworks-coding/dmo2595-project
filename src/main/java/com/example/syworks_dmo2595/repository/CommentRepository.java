package com.example.syworks_dmo2595.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostIdOrderByCommentId(Long postId);
    Comment findByCommentId(Long commentId);
    List<Comment> findAllByParentIdIsNotNullAndPostIdOrderByCommentId(Long postId);
    List<Comment> findAllByPostIdAndParentIdIsNullOrderByCommentId(Long postId);


}
