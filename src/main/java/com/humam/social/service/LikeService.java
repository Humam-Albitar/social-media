package com.humam.social.service;

import com.humam.social.payload.like.LikeResponse;

import java.util.List;

public interface LikeService {
    void toggleLike(long postId);
    List<LikeResponse> getLikesByPostId(long postId);
    Long getLikesCountByPostId(long postId);

}
