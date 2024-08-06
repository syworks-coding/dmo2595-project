package com.example.syworks_dmo2595.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    public Integer countByCommentId(Long commentId);
    public Boolean existsByCommentIdAndUserId(Long commentId, Long userId);
    public Boolean existsByPostIdAndUserId(Long postId, Long userId);
    public Like findByPostIdAndUserId(Long postId, Long userId);
    public Like findByCommentIdAndUserId(Long commentId, Long userId);
    public Long countByPostId(Long postId);
}
