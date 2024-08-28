package com.humam.social.payload.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

@Data
@Schema(
        description = "PostDto Model Information"
)
public class PostDto {

    @Schema(
            description = "Post Content"
    )
    // post content should not be null or empty
    @NotEmpty
    private String content;

}
