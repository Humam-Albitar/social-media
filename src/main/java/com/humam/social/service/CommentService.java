package com.humam.social.service;

import com.humam.social.payload.comment.CommentDto;
import com.humam.social.payload.comment.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(long postId, CommentDto commentDto);

    List<CommentResponse> getCommentsByPostId(long postId);

    CommentResponse getCommentById(Long postId, Long commentId);

    CommentResponse updateComment(Long postId, long commentId, CommentDto commentRequest);

    void deleteComment(Long postId, Long commentId);
}
