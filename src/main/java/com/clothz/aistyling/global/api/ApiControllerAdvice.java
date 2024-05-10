package com.clothz.aistyling.global.api;

import com.clothz.aistyling.global.error.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<String> bindException(BindException e) {
        return ApiResponse.error(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage()
        );
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception400.class)
    public ApiResponse<String> badRequest(final Exception400 e) {
        return ApiResponse.error(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(Exception401.class)
    public ApiResponse<String> unAuthorized(final Exception401 e) {
        return ApiResponse.error(
                HttpStatus.UNAUTHORIZED,
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(Exception403.class)
    public ApiResponse<String> forbidden(final Exception403 e) {
        return ApiResponse.error(
                HttpStatus.FORBIDDEN,
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception404.class)
    public ApiResponse<String> notFound(final Exception404 e) {
        return ApiResponse.error(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception500.class)
    public ApiResponse<String> serverError(final Exception500 e) {
        return ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<String> handleValidationExceptions(final MethodArgumentNotValidException ex) {
        final BindingResult bindingResult = ex.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        final String errorMessage = fieldErrors.stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation error");

        return ApiResponse.error(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<String> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<String> accessDeniedError(final AccessDeniedException e) {
        return ApiResponse.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<String> unknownServerError(final RuntimeException e) {
        return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}

