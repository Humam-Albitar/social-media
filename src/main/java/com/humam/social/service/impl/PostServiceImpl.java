package com.humam.social.service.impl;

import com.humam.social.exception.APIException;
import com.humam.social.exception.ResourceNotFoundException;
import com.humam.social.payload.comment.CommentResponse;
import com.humam.social.payload.like.LikeResponse;
import com.humam.social.payload.post.PostDetailDto;
import com.humam.social.payload.post.PostDto;
import com.humam.social.payload.post.PostListDto;
import com.humam.social.payload.post.PostResponse;
import com.humam.social.repository.PostRepository;
import com.humam.social.service.PostService;
import com.humam.social.entity.Post;
import com.humam.social.utils.SecurityUtils;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private ModelMapper mapper;


    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
          this.postRepository = postRepository;
          this.mapper = mapper;
    }

    @Override
    @CacheEvict(value = {"posts", "stories"},allEntries = true)
    public PostListDto createPost(PostDto postDto, boolean isStory) {

        // convert DTO to entity
        Post post = mapToEntity(postDto);
        post.setUser(SecurityUtils.getCurrentUser());
        if(isStory){
            post.setExpiredAt(LocalDateTime.now().plusHours(1));
        }
        Post newPost = postRepository.save(post);

        // convert entity to DTO
        PostListDto postResponse = new PostListDto();
        postResponse.setId(newPost.getId());
        postResponse.setContent(newPost.getContent());
        return postResponse;
    }

    @Override
    @Cacheable("posts")
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        System.out.println("no cached data for posts");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findByExpiredAtIsNull(pageable);

        // get content for page object
        List<Post> listOfPosts = posts.getContent();

        List<PostListDto> content= listOfPosts.stream().map(post ->
                new PostListDto(post.getId(),post.getContent(),post.getComments().size(),post.getLikes().size())).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }
    @Override
    @Cacheable("stories")
    public PostResponse getAllStories(int pageNo, int pageSize, String sortBy, String sortDir) {
        System.out.println("no cached data for stories");
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findByExpiredAtIsNotNullAndExpiredAtAfter(LocalDateTime.now(),pageable);


        // get content for page object
        List<Post> listOfPosts = posts.getContent();

        List<PostListDto> content= listOfPosts.stream().map(post ->
                new PostListDto(post.getId(),post.getContent(),post.getComments().size(),post.getLikes().size())).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDetailDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        PostDetailDto postDetailDto=new PostDetailDto();
        postDetailDto.setId(post.getId());
        postDetailDto.setContent(post.getContent());
        postDetailDto.setCommentsCount(post.getComments().size());
        postDetailDto.setLikesCount(post.getLikes().size());
        postDetailDto.setComments(post.getComments().stream().map(i->
                new CommentResponse(i.getId(),i.getContent(),i.getUser().getId(),i.getUser().getName(),i.getPost().getId())).collect(Collectors.toSet()));
        postDetailDto.setLikes(post.getLikes().stream().map(i->
                new LikeResponse(i.getUser().getId(),i.getUser().getName(),i.getPost().getId())).collect(Collectors.toSet()));
        return postDetailDto;
    }

    @Override
    @CacheEvict(value = {"posts", "stories"},allEntries = true)
    public PostDto updatePost(PostDto postDto, long id) {
        // get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        if(!post.getUser().getId().equals(SecurityUtils.getCurrentUser().getId())){
            throw new APIException(HttpStatus.BAD_REQUEST, "You Don't Have Permission Too Update This Post");
        }

        post.setContent(postDto.getContent());
        post.setUser(SecurityUtils.getCurrentUser());
        Post updatedPost = postRepository.save(post);
        return mapToDTO(updatedPost);
    }

    @Override
    @CacheEvict(value = {"posts", "stories"},allEntries = true)
    public void deletePostById(long id) {
        // get post by id from the database
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        if(!post.getUser().getId().equals(SecurityUtils.getCurrentUser().getId())){
            throw new APIException(HttpStatus.BAD_REQUEST, "You Don't Have Permission Too Delete This Post");
        }

        postRepository.delete(post);
    }
    @Override
    @CacheEvict(value = {"stories"},allEntries = true)
    public void removeExpiredStories() {
        List<Post> expiredStories = postRepository.findByExpiredAtIsNotNullAndExpiredAtBefore(LocalDateTime.now());
        postRepository.deleteAll(expiredStories);
    }

    // convert Entity into DTO
    private PostDto mapToDTO(Post post){
        PostDto postDto = mapper.map(post, PostDto.class);
        return postDto;
    }

    // convert DTO to entity
    private Post mapToEntity(PostDto postDto){
        Post post = mapper.map(postDto, Post.class);
        return post;
    }
}
