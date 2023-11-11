package com.example.demo.service;

import com.example.demo.dto.CommentDto;
import com.example.demo.entity.NotificationEmail;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.exception.SpringRedditException;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AuthService authService;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentDto commentDto) throws PostNotFoundException, SpringRedditException {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("No post found with id " + commentDto.getPostId()));
        commentRepository.save(commentMapper.map(commentDto, post, authService.getCurrentUser()));

        String message = mailContentBuilder.build(
                authService.getCurrentUser().getUsername() + " comments in your post.\n" + POST_URL);
        sendCommentEmail(message, authService.getCurrentUser(), post.getUser());
    }

    private void sendCommentEmail(String message, User commenter, User postOwner) throws SpringRedditException {
        NotificationEmail email = new NotificationEmail(
                commenter.getUsername() + "commented in your post",
                postOwner.getEmail(),
                message);
        mailService.sendMail(email);
    }

    public List<CommentDto> getAllCommentsForPost(Long postId) throws PostNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("No post found with id " + postId));
        return commentRepository.findAllByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getCommentsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
