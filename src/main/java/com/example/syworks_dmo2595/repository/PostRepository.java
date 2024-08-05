package com.example.syworks_dmo2595.repository;

import com.example.syworks_dmo2595.service.dto.response.PostServiceLoadPostListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByPostIdAsc();
    Post findByPostId(Long postId);
}
