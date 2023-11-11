package com.example.demo.mapper;

import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.entity.*;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.VoteRepository;
import com.example.demo.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;

    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "description", source = "postRequest.description")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(getCommentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "upVote", expression = "java(isUpvote(post))")
    @Mapping(target = "downVote", expression = "java(isDownvote(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer getCommentCount(Post post) {
        return commentRepository.findAllByPost(post).size();
    }

    String getDuration(Post post) {
        // TODO: using time ago library
//        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
        return Duration.between(post.getCreatedDate(), Instant.now()).toString();
    }

    boolean isUpvote(Post post) {
        return checkVoteType(post, VoteType.UPVOTE);
    }

    boolean isDownvote(Post post) {
        return checkVoteType(post, VoteType.DOWNVOTE);
    }

    boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser = voteRepository
                    .findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
        }
        return false;
    }
}
