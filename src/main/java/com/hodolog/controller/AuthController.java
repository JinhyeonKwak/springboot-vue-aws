package com.hodolog.controller;

import com.hodolog.domain.Users;
import com.hodolog.exception.InvalidSignInInformation;
import com.hodolog.repository.UserRepository;
import com.hodolog.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/auth/login")
    public Users login(@RequestBody Login login) {
        // json 아이디/비밀번호
        log.info(">>> login={}", login);

        // DB에서 조회
        Users user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSignInInformation::new);

        // 토큰을 응답
        return user;

    }
}
