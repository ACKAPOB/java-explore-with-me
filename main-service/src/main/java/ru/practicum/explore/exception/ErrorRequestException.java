package ru.practicum.explore.exception;

public class ErrorRequestException extends RuntimeException {
    public ErrorRequestException(String message) {
        super(message);
    }
}
