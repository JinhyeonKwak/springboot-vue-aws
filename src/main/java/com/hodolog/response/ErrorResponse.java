package com.hodolog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;


/**
 * {
 * "code"" "400",
 * "message": "잘못된 요청입니다."
 * "validation": {
 *     "title": "값을 입력해주세요."
 * }
 * }
 */
@Getter
public class ErrorResponse {

    private int code;
    private String message;
    private Map<String, String> validation;

    @Builder
    public ErrorResponse(int code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public void addValidation(String fieldName, String errorMessage) {
        if (validation == null) {
            validation = new HashMap<>();
        }
        this.validation.put(fieldName, errorMessage);
    }

}
