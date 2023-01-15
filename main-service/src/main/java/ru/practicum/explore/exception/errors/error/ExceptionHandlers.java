package ru.practicum.explore.exception.errors.error;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.exception.*;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFound(ObjectNotFoundException e) {
        return ApiError.builder()
                .message(e.getLocalizedMessage())
                .reason("The required object was not found.")
                .status(String.valueOf(HttpStatus.NOT_FOUND))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ErrorRequestException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError forbidden(RuntimeException e) {
        return ApiError.builder()
                .message(e.getLocalizedMessage())
                .reason("For the requested operation the conditions are not met.")
                .status(String.valueOf(HttpStatus.FORBIDDEN))
                .timestamp(LocalDateTime.now())
                .build();
    }
    ///////////////////////////////////////////////

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(ConflictException exc) {
        log.info("handleConflictException HttpStatus.CONFLICT");
        return ApiError.builder()
                .message(exc.getMessage())
                .reason("Error - ConflictException")
                .status(String.valueOf(HttpStatus.CONFLICT))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException exc) {
        return ApiError.builder()
                .message(exc.getMessage())
                .reason("Error occurred BAD_REQUEST")
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .timestamp(LocalDateTime.now())
                .build();
    }
}


