package com.sprta.jamrello.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponseDto {

    private final String msg;
    private final HttpStatus status;

    public ExceptionResponseDto(ErrorCode e) {
        this.msg = e.getMsg();
        this.status = e.getStatus();
    }
}
