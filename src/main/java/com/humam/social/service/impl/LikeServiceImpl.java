package com.humam.social.service.impl;

import com.humam.social.entity.Like;
import com.humam.social.entity.Post;
import com.humam.social.entity.User;
import com.humam.social.exception.ResourceNotFoundException;
import com.humam.social.payload.like.LikeResponse;
import com.humam.social.repository.LikeRepository;
import com.humam.social.repository.PostRepository;
import com.humam.social.service.LikeService;
import com.humam.social.utils.SecurityUtils;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    private LikeRepository likeRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;
    public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository, ModelMapper mapper) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    @CacheEvict(value = {"posts", "stories"},allEntries = true)
    public void toggleLike(long postId) {
        List<Like> likes=likeRepository.findByPostIdAndUser(postId, SecurityUtils.getCurrentUser());
        if(likes.isEmpty()) {
            Like like = new Like();

            // retrieve post entity by id
            Post post = postRepository.findById(postId).orElseThrow(
                    () -> new ResourceNotFoundException("Post", "id", postId));

            // set post to like entity
            like.setPost(post);
            like.setUser(SecurityUtils.getCurrentUser());
            // like entity to DB
            Like newLike = likeRepository.save(like);
        }
        else{
            likeRepository.deleteAll(likes);
        }
    }

    @Override
    public List<LikeResponse> getLikesByPostId(long postId) {
        // retrieve likes by postId
        List<Like> likes = likeRepository.findByPostId(postId);

        // convert list of like entities to list of comment dto's
        return likes.stream().map(like -> mapToDTO(like)).collect(Collectors.toList());
    }

    @Override
    public Long getLikesCountByPostId(long postId) {
        User user=SecurityUtils.getCurrentUser();
        return likeRepository.countByPostIdAndUser(postId,user);
    }


    private LikeResponse mapToDTO(Like like){
        LikeResponse likeResponse = new LikeResponse();
        likeResponse.setPostId(like.getPost().getId());
        likeResponse.setUserId(like.getUser().getId());
        likeResponse.setUserName(like.getUser().getName());
        return  likeResponse;
    }

}
