package com.clothz.aistyling.global.error;

import com.clothz.aistyling.global.api.ApiResponse;
import org.springframework.http.HttpStatus;

public class Exception404 extends RuntimeException{
    public Exception404(final ErrorCode code) {
        super(code.getMessage());
    }
    public ApiResponse<Exception404> body() {
        return ApiResponse.error(HttpStatus.NOT_FOUND, getMessage());
    }
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}
