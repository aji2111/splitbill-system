package com.splitbill.expense_service.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.splitbill.expense_service.dto.response.BaseResponse;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            IllegalArgumentException.class
    )
    public ResponseEntity<BaseResponse<Object>>
    handleIllegalArgumentException(
            IllegalArgumentException ex
    ) {

        BaseResponse<Object> response =
                BaseResponse.builder()
                        .status("FAILED")
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .data(null)
                        .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(
            EntityNotFoundException.class
    )
    public ResponseEntity<BaseResponse<Object>>
    handleEntityNotFoundException(
            EntityNotFoundException ex
    ) {

        BaseResponse<Object> response =
                BaseResponse.builder()
                        .status("FAILED")
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .data(null)
                        .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<BaseResponse<Object>>
    handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        String message =
                ex.getBindingResult()
                        .getFieldError()
                        .getDefaultMessage();

        BaseResponse<Object> response =
                BaseResponse.builder()
                        .status("FAILED")
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .data(null)
                        .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(
            Exception.class
    )
    public ResponseEntity<BaseResponse<Object>>
    handleGeneralException(
            Exception ex
    ) {

        BaseResponse<Object> response =
                BaseResponse.builder()
                        .status("ERROR")
                        .message(
                                "Internal server error"
                        )
                        .timestamp(LocalDateTime.now())
                        .data(null)
                        .build();

        return ResponseEntity
                .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(response);
    }
}

