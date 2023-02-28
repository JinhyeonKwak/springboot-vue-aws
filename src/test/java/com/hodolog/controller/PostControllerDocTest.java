package com.hodolog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.domain.Post;
import com.hodolog.repository.PostRepository;
import com.hodolog.request.PostCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.hodolman.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private PostCreate request;
    private String json;

    @Test
    @DisplayName("게시글 단건 조회")
    void test1() throws Exception {
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-get-one",
                        pathParameters(
                                parameterWithName("postId").description("게시글 ID")),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        ))
                );

    }

    @Test
    @DisplayName("게시글 등록")
    void test2() throws Exception {

        request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath("title").description("글 제목")
                                        .attributes(key("constraint").value("좋은 제목을 입력해주세요.")),
                                fieldWithPath("content").description("글 내용").optional()
                        ))
                );

    }
}
