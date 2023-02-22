package com.hodolog.service;

import com.hodolog.domain.Post;
import com.hodolog.repository.PostRepository;
import com.hodolog.request.PostCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clear() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        postService.write(postCreate);
        Post post = postRepository.findAll().get(0);

        assertThat(postRepository.count()).isEqualTo(1L);
        assertThat(post.getTitle()).isEqualTo("제목입니다.");
        assertThat(post.getContent()).isEqualTo("내용입니다.");

    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);
        Post findPost = postService.get(post.getId());

        assertThat(findPost).isNotNull();
        assertThat(postRepository.count()).isEqualTo(1L);
        assertThat("foo").isEqualTo(findPost.getTitle());
        assertThat("bar").isEqualTo(findPost.getContent());
    }
}