package com.humam.social.payload.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostListDto {
    private long id;
    private String content;
    private long commentsCount = 0;
    private long likesCount = 0;
}
