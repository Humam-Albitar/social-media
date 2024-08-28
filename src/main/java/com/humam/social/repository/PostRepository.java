package com.humam.social.repository;

import com.humam.social.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByExpiredAtIsNotNullAndExpiredAtAfter(LocalDateTime dateTime, Pageable pageable);
    List<Post> findByExpiredAtIsNotNullAndExpiredAtBefore(LocalDateTime dateTime);

    Page<Post> findByExpiredAtIsNull(Pageable pageable);

}
