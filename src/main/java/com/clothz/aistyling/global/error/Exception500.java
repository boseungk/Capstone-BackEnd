package com.clothz.aistyling.global.error;

import com.clothz.aistyling.global.api.ApiResponse;
import org.springframework.http.HttpStatus;

public class Exception500 extends RuntimeException{
    public Exception500(final ErrorCode code) {
        super(code.getMessage());
    }
    public ApiResponse<Exception500> body() {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, getMessage());
    }
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
