package com.humam.social.service;

import com.humam.social.payload.post.PostDetailDto;
import com.humam.social.payload.post.PostListDto;
import com.humam.social.payload.post.PostResponse;
import com.humam.social.payload.post.PostDto;

public interface PostService {
    PostListDto createPost(PostDto postDto, boolean isStory);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    PostResponse getAllStories(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDetailDto getPostById(long id);

    PostDto updatePost(PostDto postDto, long id);

    void deletePostById(long id);

    public void removeExpiredStories();

}
