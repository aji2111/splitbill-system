package com.splitbill.expense_service.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BaseResponse<T> {

private String status;

private String message;

private LocalDateTime timestamp;

private T data;

}
