package com.humam.social.payload.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private long id;
    private String content;
    private long userId;
    private String userName;
    private long postId;
}
