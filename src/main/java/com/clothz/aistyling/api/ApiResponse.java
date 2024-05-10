package com.clothz.aistyling.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Schema(description = "HTTP 응답 상태")
public class ApiResponse<T> {
    @Schema(description = "HTTP 응답 상태에 대한 숫자 코드")
    private final int code;
    @Schema(description = "HTTP 응답 상태를 담고 있는 객체")
    private final HttpStatus status;
    @Schema(description = "HTTP 응답 상태에 대한 메시지")
    private final String message;
    @Schema(description = "Response 데이터")
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
