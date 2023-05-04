package ru.yandex.practicum.filmorate.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.exception.ObjectAlreadyExistException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFailValidation(final MethodArgumentNotValidException e) {
        StringBuilder errorMessage = new StringBuilder();
        for (int i = 0; i < e.getBindingResult().getFieldErrorCount(); i++) {
            errorMessage.append(e.getBindingResult().getFieldErrors().get(i).getField() + " ");
            errorMessage.append(e.getBindingResult().getFieldErrors().get(i).getDefaultMessage() + ";");
        }
        return new ErrorResponse(errorMessage.toString());
    }

    @ExceptionHandler({DataIntegrityViolationException.class, EntityNotExistException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleError(final Throwable e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ErrorResponse alreadyExistException(final ObjectAlreadyExistException e) {
        return new ErrorResponse(e.getMessage());
    }
}

