package com.clothz.aistyling.global.error;

import com.clothz.aistyling.global.api.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Exception400 extends RuntimeException {
    public Exception400(final String message) {
        super(message);
    }
    public ApiResponse<Exception400> body() {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, getMessage());
    }
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }
}

