package com.sparta.jamrello.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdviceController {

    @ExceptionHandler(BisException.class)
    public ResponseEntity<ExceptionResponseDto> bisExceptionHandler(BisException e) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(e.getErrorCode());
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String msg = bindingResult.getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(BAD_REQUEST).body(
                new ExceptionResponseDto(BAD_REQUEST, msg)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDto> illegalArgumentExceptionHandler(
            IllegalArgumentException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(FORBIDDEN.value()).body(
                new ExceptionResponseDto(FORBIDDEN, ex.getMessage())
        );
    }

}
