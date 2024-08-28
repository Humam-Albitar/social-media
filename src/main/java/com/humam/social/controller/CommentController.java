package com.humam.social.controller;

import com.humam.social.payload.comment.CommentDto;
import com.humam.social.payload.comment.CommentResponse;
import com.humam.social.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@SecurityRequirement(

        name = "X-API-KEY"
)
@SecurityRequirement(
        name = "Bear Authentication"
)
@Tag(
        name = "Comment APIs for Post Resource"
)
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(
            summary = "Add Comment to REST API",
            description = "Add Comment to REST API is used to Insert post comment into the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping("/posts/{postId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CommentResponse> createComment(@PathVariable(value = "postId") long postId,
                                                         @Valid @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get All Comments of Posts REST API",
            description = "Get All Comments of Posts REST API is used to get all comments of post from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/posts/{postId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<CommentResponse> getCommentsByPostId(@PathVariable(value = "postId") Long postId){
        return commentService.getCommentsByPostId(postId);
    }

    @Operation(
            summary = "Get Comment Details REST API",
            description = "Add Comment Details REST API is used to get comment details from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/posts/{postId}/{commentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable(value = "postId") Long postId,
                                                     @PathVariable(value = "commentId") Long commentId){
        CommentResponse commentDto = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }
    @Operation(
            summary = "Update Comment Details REST API",
            description = "Update Comment Details REST API is used to update comment in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PutMapping("/posts/{postId}/{commentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable(value = "postId") Long postId,
                                                    @PathVariable(value = "commentId") Long commentId,
                                                    @Valid @RequestBody CommentDto commentDto){
        CommentResponse updatedComment = commentService.updateComment(postId, commentId, commentDto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }
    @Operation(
            summary = "Delete Comment REST API",
            description = "Delete Comment REST API is used to delete comment from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @DeleteMapping("/posts/{postId}/{commentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                                                @PathVariable(value = "commentId") Long commentId){
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }
}
