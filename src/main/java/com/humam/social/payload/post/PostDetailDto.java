package com.humam.social.payload.post;

import com.humam.social.payload.comment.CommentResponse;
import com.humam.social.payload.like.LikeResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailDto {
    private long id;
    private String content;
    private Set<CommentResponse> comments;
    private Set<LikeResponse> likes;
    private long commentsCount;
    private long likesCount;
}
