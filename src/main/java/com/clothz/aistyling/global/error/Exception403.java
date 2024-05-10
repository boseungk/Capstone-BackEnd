package com.clothz.aistyling.global.error;

import com.clothz.aistyling.global.api.ApiResponse;
import org.springframework.http.HttpStatus;

public class Exception403 extends RuntimeException{
    public Exception403(final String message) {
        super(message);
    }
    public ApiResponse<Exception403> body() {
        return ApiResponse.error(HttpStatus.FORBIDDEN, getMessage());
    }
    public HttpStatus status() {
        return HttpStatus.FORBIDDEN;
    }
}
