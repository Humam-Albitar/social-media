package com.humam.social.controller;


import com.humam.social.payload.like.LikeResponse;
import com.humam.social.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/likes")
@SecurityRequirement(
        name = "X-API-KEY"
)
@SecurityRequirement(
        name = "Bear Authentication"
)
@Tag(
        name = "Like APIs for Post Resource"
)
public class LikeController {

    private LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @Operation(
            summary = "Add or Remove Like on Post REST API",
            description = "Add or Remove Like on Post REST API is used to Add or Remove Like on Post in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping("/posts/{postId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> toggleLike(@PathVariable(value = "postId") long postId){
        likeService.toggleLike(postId);
        return new ResponseEntity<>("Done successfully", HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get All Likes on Post REST API",
            description = "Get All Likes on Post REST API is used to Get all Likes on Post from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/posts/{postId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<LikeResponse> getLikesByPostId(@PathVariable(value = "postId") Long postId){
        return likeService.getLikesByPostId(postId);
    }

}
