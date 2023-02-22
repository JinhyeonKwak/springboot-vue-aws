package com.hodolog.domain;

import com.hodolog.request.PostCreate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    public static Post createPost(PostCreate postCreate) {
        return Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
    }
}
