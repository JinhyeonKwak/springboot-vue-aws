package com.hodolog.response;

import com.hodolog.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 서비스 정책에 맞는 응답 클래스
 */
@Getter
@Builder
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String content;

    /**
     * Post 엔티티를 입력으로 받고, 응답 DTO를 반환한다.
     * (타이틀은 최대 10글자까지)
     * @param post
     * @return
     */
    public static PostResponse createPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle()
                        .substring(0, Math.min(post.getTitle().length(), 10)))
                .content(post.getContent())
                .build();
    }
}
