package com.example.demo.service;

import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.entity.Post;
import com.example.demo.entity.Subreddit;
import com.example.demo.entity.User;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.exception.SubredditNotFoundException;
import com.example.demo.mapper.PostMapper;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.SubredditRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
    private PostRepository postRepository;
    private SubredditRepository subredditRepository;
    private UserRepository userRepository;
    private AuthService authService;
    private PostMapper postMapper;

    public void save(PostRequest postRequest) throws SubredditNotFoundException {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(
                        "No subreddit found with name " + postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public PostResponse getPost(Long id) throws PostNotFoundException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("No post found with id " + id));
        return postMapper.mapToDto(post);
    }

    public List<PostResponse> getPostsBySubreddit(Long subredditId) throws SubredditNotFoundException {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(
                        "No subreddit found with id " + subredditId
                ));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No user found with username " + username
                ));
        List<Post> posts = postRepository.findAllByUser(user);
        return posts.stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
