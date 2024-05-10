package com.clothz.aistyling.global.error;

import com.clothz.aistyling.global.api.ApiResponse;
import org.springframework.http.HttpStatus;

public class Exception401 extends RuntimeException{
    public Exception401(final ErrorCode code) {
        super(code.getMessage());
    }
    public ApiResponse<Exception401> body() {
        return ApiResponse.error(HttpStatus.UNAUTHORIZED, getMessage());
    }
    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}
