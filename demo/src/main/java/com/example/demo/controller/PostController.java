package com.example.demo.controller;

import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.exception.SubredditNotFoundException;
import com.example.demo.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) throws SubredditNotFoundException {
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) throws PostNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getPost(id));
    }

    @GetMapping("/by-subreddit/{subredditId}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long subredditId) throws SubredditNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getPostsBySubreddit(subredditId));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username) throws UsernameNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getPostsByUsername(username));
    }
}
