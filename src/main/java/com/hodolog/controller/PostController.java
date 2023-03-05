package com.hodolog.controller;

import com.hodolog.config.data.UserSession;
import com.hodolog.exception.InvalidRequest;
import com.hodolog.request.PostCreate;
import com.hodolog.request.PostEdit;
import com.hodolog.request.PostSearch;
import com.hodolog.response.PostResponse;
import com.hodolog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    // SSR -> jsp, thymeleaf, mustache, freemarker
    // SPA ->
    // vue -> vue + SSR = nuxt
    // react -> react + SSR = next

    // Http method
    // GET, POST, PUT, PATCH, DELETE, CONNECT, OPTIONS, HEAD, TRACE

    private final PostService postService;

    @GetMapping("/foo")
    public Long foo(UserSession userSession) {
        log.info(">>> {}", userSession.getId());
        return userSession.getId();
    }

    @GetMapping("/bar")
    public String bar() {
        return "인증이 필요 없는 페이지";
    }

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) throws Exception {

        // 데이터를 검증하는 이유
        // 1. client 개발자가 깜빡할 수 있다. 실수로 값을 안 보낼 수 있다.
        // 2. client bug로 값이 누락될 수 있다.
        // 3. 외부에서 값을 임의로 조작해서 보낼 수 있다.
        // 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
        // 5. 서버 개발자의 편안함을 위해서
        request.validate();
        postService.write(request);

        /*
        String title = params.getTitle();
        if (title == null || title.equals("")) {
            // 1. 빡세다 (노가다)
            // 2. 개발팁 -> 무언가 3번 이상 반복 작업을 할 때 내가 뭔가 잘못하고 있는 건 아닐지 의심한다.
            // 3. 누락 가능성
            // 4. 생각보다 검증해야 할 게 많다. (꼼꼼하지 않을 수 있다.)
            // 5. 뭔가 개발자스럽지 않다 -> 간지 x
            throw new Exception("타이틀 값이 없어요!");
        }

        String content = params.getContent();
        if (content == null || content.equals("")) {
            // error
        }
        */
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getPosts(@ModelAttribute PostSearch postSearch) {
        return postService.getPosts(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse updatePost(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit) {
        return postService.edit(postId, postEdit);
    }

    @DeleteMapping("/posts/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
