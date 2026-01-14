package ru.java.filesharing.web.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.java.filesharing.constants.Constants;
import ru.java.filesharing.exception.*;
import ru.java.filesharing.web.dto.error.ApiErrorResponse;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiErrorResponseHandlerController {
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return new ApiErrorResponse(
            HttpStatus.NOT_FOUND,
            e.getMessage()
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            e.getMessage()
        );
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleFileNotFoundException(FileNotFoundException e) {
        return new ApiErrorResponse(
            HttpStatus.NOT_FOUND,
            e.getMessage()
        );
    }

    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorResponse handleAccessDeniedException() {
        return new ApiErrorResponse(
            HttpStatus.FORBIDDEN,
            Constants.ACCESS_DENIED_MESSAGE
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getDefaultMessage,
                (msg1, msg2) -> msg1 + "; " + msg2
            ));
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            Constants.VALIDATION_FAILED_MESSAGE,
            errors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errors = e.getConstraintViolations().stream()
            .collect(Collectors.toMap(
                violation -> violation.getPropertyPath().toString(),
                ConstraintViolation::getMessage,
                (msg1, msg2) -> msg1 + "; " + msg2
            ));
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            Constants.VALIDATION_FAILED_MESSAGE,
            errors
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleAuthenticationException() {
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            Constants.AUTHENTICATION_FAILED_MESSAGE
        );
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleFileUploadException(FileUploadException e) {
        return new ApiErrorResponse(
            HttpStatus.BAD_REQUEST,
            e.getMessage()
        );
    }

    @ExceptionHandler(FileDeleteException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleFileDeleteException(FileDeleteException e) {
        return new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            e.getMessage()
        );
    }
}
