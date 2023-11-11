package com.example.demo.mapper;

import com.example.demo.dto.CommentDto;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    Comment map(CommentDto commentDto, Post post, User user);

    @Mapping(target = "postId", source = "post.postId")
    @Mapping(target = "username", source = "user.username")
    CommentDto mapToDto(Comment comment);
}
