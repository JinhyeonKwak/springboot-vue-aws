package com.hodolog.service;

import com.hodolog.domain.Post;
import com.hodolog.repository.PostRepository;
import com.hodolog.request.PostCreate;
import com.hodolog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void write(PostCreate postCreate) {
        Post post = Post.createPost(postCreate);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다"));
        return PostResponse.createPostResponse(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable).stream()
                .map(PostResponse::createPostResponse)
                .collect(Collectors.toList());
    }
}
