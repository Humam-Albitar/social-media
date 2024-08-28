package com.humam.social.repository;

import com.humam.social.entity.Like;
import com.humam.social.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByPostId(long postId);
    List<Like> findByPostIdAndUser(long postId, User user);
    long countByPostIdAndUser(long postId, User user);
}
