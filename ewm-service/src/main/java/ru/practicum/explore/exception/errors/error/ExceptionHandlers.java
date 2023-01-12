package ru.practicum.explore.exception.errors.error;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.exception.ApiError;
import ru.practicum.explore.exception.ForbiddenRequestException;
import ru.practicum.explore.exception.ObjectNotFoundException;

import java.time.LocalDateTime;

/**
 * Обработка ошибок
 */
@RestControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFound(RuntimeException e) {
        return ApiError.builder()
                .message(e.getLocalizedMessage())
                .reason("The required object was not found.")
                .status(String.valueOf(HttpStatus.NOT_FOUND))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ForbiddenRequestException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError forbidden(RuntimeException e) {
        return ApiError.builder()
                .message(e.getLocalizedMessage())
                .reason("For the requested operation the conditions are not met.")
                .status(String.valueOf(HttpStatus.FORBIDDEN))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError notFound(Throwable e) {
        return ApiError.builder()
                .message(e.getLocalizedMessage())
                .reason("Error occurred")
                .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                .timestamp(LocalDateTime.now())
                .build();
    }
}

