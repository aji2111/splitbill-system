package com.splitbill.auth_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.splitbill.auth_service.dto.response.BaseResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BaseResponse<Object>>
    handleBadRequest(
            BadRequestException ex
    ) {

        BaseResponse<Object> response =
                BaseResponse.builder()
                        .message(ex.getMessage())
                        .data(null)
                        .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


@ExceptionHandler(
        MethodArgumentNotValidException.class
)
public ResponseEntity<BaseResponse<Object>>
handleValidationException(
        MethodArgumentNotValidException ex
) {

    String errorMessage = ex

            .getBindingResult()

            .getFieldError()

            .getDefaultMessage();

    BaseResponse<Object> response =

            BaseResponse.builder()

                    .message(errorMessage)

                    .data(null)

                    .build();

    return ResponseEntity

            .status(HttpStatus.BAD_REQUEST)

            .body(response);
}




}

