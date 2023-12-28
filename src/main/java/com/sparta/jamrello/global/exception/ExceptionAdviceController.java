package com.sparta.jamrello.global.exception;

import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ExceptionAdviceController {

    @ExceptionHandler(BisException.class)
    public ResponseEntity<ExceptionResponseDto> bisExceptionHandler(BisException e) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(e.getErrorCode());
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }


//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ExceptionResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
//        BindingResult bindingResult = ex.getBindingResult();
//        StringBuilder builder = new StringBuilder();
//        for (FieldError fieldError : bindingResult.getFieldErrors()) {
//            builder.append(fieldError.getDefaultMessage());
//        }
//      return ResponseEntity.status(ex.getStatusCode()).body(
//            new ExceptionResponseDto(
//                (HttpStatus) ex.getStatusCode(), builder.toString())
//        );
//    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDto> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(
            new ExceptionResponseDto(HttpStatus.FORBIDDEN, ex.getMessage())
        );
    }

}
