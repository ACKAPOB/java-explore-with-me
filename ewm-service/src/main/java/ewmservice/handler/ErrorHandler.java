package ewmservice.handler;

import ewmservice.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(final EntityNotFoundException e) {
        log.info("404 {}", e.getMessage(), e);
        return new ErrorResponse(List.of(e.getMessage()), "Не удалось найти.",
                ErrorState.NOT_FOUND, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.info("400 {}", e.getMessage(), e);
        return new ErrorResponse(List.of(e.getMessage()), "Некорректный запрос к серверу.",
                ErrorState.BAD_REQUEST, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse defaultHandle(final Exception e) {
        log.info("500 {}", e.getMessage(), e);
        return new ErrorResponse(List.of(e.getMessage()), "Проблема на стороне сервера.",
                ErrorState.INTERNAL_SERVER_ERROR, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        log.info("409 {}", e.getMessage(), e);
        return new ErrorResponse(List.of(e.getMessage()), "Некорректное изменение поля.",
                ErrorState.BAD_REQUEST, LocalDateTime.now());
    }
}
