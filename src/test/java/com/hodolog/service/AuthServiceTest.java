package com.hodolog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.domain.User;
import com.hodolog.repository.SessionRepository;
import com.hodolog.repository.UserRepository;
import com.hodolog.request.Login;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private User user;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void createUser() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder().
                name("곽진현")
                .email("wlsgus555@hanyang.ac.kr")
                .password("1234")
                .build());
    }

    @Test
    @Transactional // test 환경에서 @Transactional 사용하면 오염될 위험 있음
    @DisplayName("로그인 성공 후 세션 1개 생성")
    void test() throws Exception {

        Login login = Login.builder()
                .email("wlsgus555@hanyang.ac.kr")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // 트랜잭션 1
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()))
                .andDo(print());

        // 트랜잭션 2
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(RuntimeException::new);

        // 트랜잭션 없음 (getSessions -> lazy loading), test 메서드 레벨에서 트랜잭션 걸어줘야 함
        assertThat(findUser.getSessions().size()).isEqualTo(1);
    }
}