package com.humam.social.controller;

import com.humam.social.payload.post.PostDetailDto;
import com.humam.social.payload.post.PostDto;
import com.humam.social.payload.post.PostListDto;
import com.humam.social.payload.post.PostResponse;
import com.humam.social.service.PostService;
import com.humam.social.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/posts")
@Tag(
        name = "CRUD REST APIs for Post Resource"
)
@SecurityRequirement(
        name = "X-API-KEY"
)
@SecurityRequirement(
        name = "Bear Authentication"
)
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(
            summary = "Create Post REST API",
            description = "Create Post REST API is used to save post into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    // create post rest api
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<PostListDto> createPost(@Valid @RequestBody PostDto postDto,
                                                  @RequestParam(value = "isStory", defaultValue = AppConstants.DEFAULT_IS_POST_STORY, required = false) boolean isStory){
        return new ResponseEntity<>(postService.createPost(postDto,isStory), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get Feed of Posts REST API",
            description = "Get Feed of Posts REST API is used to fetch the posts from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    // get all posts rest api
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    @Operation(
            summary = "Get Feed of Stories REST API",
            description = "Get Feed of Stories REST API is used to fetch  the Stories from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    // get all posts rest api
    @GetMapping("/stories")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public PostResponse getAllStories(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return postService.getAllStories(pageNo, pageSize, sortBy, sortDir);
    }

    @Operation(
            summary = "Get Post Details REST API",
            description = "Get Post By Id REST API is used to get single post from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    // get post by id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PostDetailDto> getPostById(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Operation(
            summary = "update Post REST API",
            description = "Update Post REST API is used to update a particular post in the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    // update post by id rest api
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable(name = "id") long id){

       PostDto postResponse = postService.updatePost(postDto, id);

       return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Post REST API",
            description = "Delete Post REST API is used to delete a particular post from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    // delete post rest api
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){

        postService.deletePostById(id);

        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }

}
