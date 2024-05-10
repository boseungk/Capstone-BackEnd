package com.clothz.aistyling.global.error;

import com.clothz.aistyling.global.api.ApiResponse;
import org.springframework.http.HttpStatus;

public class Exception409 extends RuntimeException{
    public Exception409(final ErrorCode code) {
        super(code.getMessage());
    }
    public ApiResponse<Exception404> body() {
        return ApiResponse.error(HttpStatus.CONFLICT, getMessage());
    }
    public HttpStatus status() {
        return HttpStatus.CONFLICT;
    }
}

