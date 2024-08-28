package com.humam.social.payload.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponse {
    private long userId;
    private String userName;
    private long postId;
}
