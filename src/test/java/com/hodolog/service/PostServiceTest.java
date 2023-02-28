package com.hodolog.service;

import com.hodolog.domain.Post;
import com.hodolog.exception.PostNotFound;
import com.hodolog.repository.PostRepository;
import com.hodolog.request.PostCreate;
import com.hodolog.request.PostEdit;
import com.hodolog.request.PostSearch;
import com.hodolog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
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
        PostResponse response = postService.get(post.getId());

        assertThat(response).isNotNull();
        assertThat(postRepository.count()).isEqualTo(1L);
        assertThat("foo").isEqualTo(response.getTitle());
        assertThat("bar").isEqualTo(response.getContent());
    }

    @Test
    @DisplayName("타이틀이 10글자 이상인 경우 10글자까지만 보여준다.")
    void test3() {
        Post post = Post.builder()
                .title("12345678901111")
                .content("bar")
                .build();
        postRepository.save(post);
        PostResponse response = postService.get(post.getId());

        assertThat(response).isNotNull();
        assertThat(postRepository.count()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("1234567890");
        assertThat(response.getContent()).isEqualTo("bar");
    }

    @Test
    @DisplayName("타이틀이 10글자 미만인 경우 타이틀을 그대로 보여준다.")
    void test4() {
        Post post = Post.builder()
                .title("12345")
                .content("bar")
                .build();
        postRepository.save(post);
        PostResponse response = postService.get(post.getId());

        assertThat(response).isNotNull();
        assertThat(postRepository.count()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("12345");
        assertThat(response.getContent()).isEqualTo("bar");
    }

    @Test
    @DisplayName("글 여러 개 조회")
    void test5() {
        Post post1 = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        Post post2 = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.saveAll(List.of(post1, post2));

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();
        List<PostResponse> findPosts = postService.getPosts(postSearch);

        assertThat(findPosts.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test6() {

        List<Post> posts = IntStream.range(0, 30)
                .mapToObj(i -> Post.builder()
                        .title("title" + i)
                        .content("content" + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(posts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();
        List<PostResponse> findPosts = postService.getPosts(postSearch);

        assertThat(findPosts.size()).isEqualTo(10);
        assertThat(findPosts.get(0).getTitle()).isEqualTo("title29");
        assertThat(findPosts.get(4).getTitle()).isEqualTo("title25");

    }

    @Test
    @DisplayName("글 제목 수정")
    void test7() {

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("새로운 제목입니다.")
                .content("내용입니다.")
                .build();

        postService.edit(post.getId(), postEdit);

        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 글입니다."));
        assertThat(changePost.getTitle()).isEqualTo("새로운 제목입니다.");
        assertThat(changePost.getContent()).isEqualTo("내용입니다.");

    }

    @Test
    @DisplayName("글 제목 수정할 때 내용을 null 값으로 넘겨도 기존의 값을 유지해야 한다.")
    void test8() {

        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("새로운 제목입니다.")
                .build();

        postService.edit(post.getId(), postEdit);

        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 글입니다."));
        assertThat(changePost.getTitle()).isEqualTo("새로운 제목입니다.");
        assertThat(changePost.getContent()).isEqualTo("내용입니다.");

    }

    @Test
    @DisplayName("게시글 삭제")
    void test9() {
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        postService.delete(post.getId());

        assertThat(postRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("글 1개 조회할 때 존재하지 않는 글이면 PostNotFound 예외를 던진다.")
    void test10() {
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        assertThatThrownBy(() -> {
            postService.get(post.getId() + 1L);
        }).isInstanceOf(PostNotFound.class)
                .hasMessage("존재하지 않는 글입니다.");
    }

    @Test
    @DisplayName("글 1개 삭제할 때 존재하지 않는 글이면 PostNotFound 예외를 던진다.")
    void test11() {
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        assertThatThrownBy(() -> {
            postService.delete(post.getId() + 1L);
        }).isInstanceOf(PostNotFound.class)
                .hasMessage("존재하지 않는 글입니다.");
    }

    @Test
    @DisplayName("글 1개 수정할 때 존재하지 않는 글이면 PostNotFound 예외를 던진다.")
    void test12() {
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        assertThatThrownBy(() -> {
            PostEdit postEdit = PostEdit.builder()
                    .title("새로운 제목입니다.")
                    .build();

            postService.edit(post.getId() + 1L, postEdit);
        }).isInstanceOf(PostNotFound.class)
                .hasMessage("존재하지 않는 글입니다.");
    }
}