package com.example.demo.controller;

import com.example.demo.dto.CommentDto;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.exception.SpringRedditException;
import com.example.demo.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentController {
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComments(@RequestBody CommentDto commentDto)
            throws PostNotFoundException, SpringRedditException {
        commentService.save(commentDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/by-postId/{postId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@PathVariable Long postId) throws PostNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentDto>> getCommentsByUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getCommentsByUsername(username));
    }
}
