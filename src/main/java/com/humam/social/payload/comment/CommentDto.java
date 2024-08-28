package com.humam.social.payload.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
@Schema(
        description = "CommentDto Model Information"
)
public class CommentDto {
    @Schema(
            description = "Comment Content"
    )
    // comment body should not be bull or empty
    // Comment body must be minimum 10 characters
    @NotEmpty
    @Size(min = 10, message = "Comment content must be minimum 10 characters")
    private String content;
}
