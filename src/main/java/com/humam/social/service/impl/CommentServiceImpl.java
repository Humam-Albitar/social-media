package com.humam.social.service.impl;

import com.humam.social.exception.APIException;
import com.humam.social.exception.ResourceNotFoundException;
import com.humam.social.payload.comment.CommentDto;
import com.humam.social.repository.CommentRepository;
import com.humam.social.repository.PostRepository;
import com.humam.social.service.CommentService;
import com.humam.social.entity.Comment;
import com.humam.social.entity.Post;
import com.humam.social.payload.comment.CommentResponse;
import com.humam.social.utils.SecurityUtils;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    @Override
    @CacheEvict(value = {"posts", "stories"},allEntries = true)
    public CommentResponse createComment(long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // set post to comment entity
        comment.setPost(post);
        comment.setUser(SecurityUtils.getCurrentUser());
        // comment entity to DB
        Comment newComment =  commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentResponse> getCommentsByPostId(long postId) {
        // retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);

        // convert list of comment entities to list of comment dto's
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentResponse getCommentById(Long postId, Long commentId) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new APIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return mapToDTO(comment);
    }

    @Override
    @CacheEvict(value = {"posts", "stories"},allEntries = true)
    public CommentResponse updateComment(Long postId, long commentId, CommentDto commentRequest) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new APIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }
        if(!comment.getUser().getId().equals(SecurityUtils.getCurrentUser().getId())){
            throw new APIException(HttpStatus.BAD_REQUEST, "You Don't Have Permission Too Edit This Comment");
        }

        comment.setContent(commentRequest.getContent());
        comment.setUser(SecurityUtils.getCurrentUser());

        Comment updatedComment = commentRepository.save(comment);
        return mapToDTO(updatedComment);
    }

    @Override
    @CacheEvict(value = {"posts", "stories"},allEntries = true)
    public void deleteComment(Long postId, Long commentId) {
        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new APIException(HttpStatus.BAD_REQUEST, "Comment does not belongs to post");
        }
        if(!comment.getUser().getId().equals(SecurityUtils.getCurrentUser().getId())){
            throw new APIException(HttpStatus.BAD_REQUEST, "You Don't Have Permission Too Delete This Comment");
        }
        commentRepository.delete(comment);
    }

    private CommentResponse mapToDTO(Comment comment){
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setPostId(comment.getPost().getId());
        commentResponse.setUserId(comment.getUser().getId());
        commentResponse.setContent(comment.getContent());
        commentResponse.setUserName(comment.getUser().getName());

        return  commentResponse;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = mapper.map(commentDto, Comment.class);
        return  comment;
    }
}
