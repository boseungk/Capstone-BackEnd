package com.clothz.aistyling.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private final int code;
    private final HttpStatus status;
    private final String message;
    private final T data;

    private ApiResponse(final HttpStatus status, final String message, final T data) {
        code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(final HttpStatus httpStatus, final String message, final T data) {
        return new ApiResponse<>(httpStatus, message, data);

    }

    public static <T> ApiResponse<T> ok(final T data) {
        return of(HttpStatus.OK, HttpStatus.OK.name(), data);
    }
}
