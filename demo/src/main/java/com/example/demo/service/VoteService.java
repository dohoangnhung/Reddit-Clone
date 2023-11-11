package com.example.demo.service;

import com.example.demo.dto.VoteDto;
import com.example.demo.entity.Post;
import com.example.demo.entity.Vote;
import com.example.demo.entity.VoteType;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.exception.SpringRedditException;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {
    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final AuthService authService;

    public void vote(VoteDto voteDto) throws PostNotFoundException, SpringRedditException {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Not post with id " + voteDto.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository
                .findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already " + voteDto.getVoteType() + " for this post");
        }

        if (VoteType.UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
