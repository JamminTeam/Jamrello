package com.sparta.jamrello.global.exception;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 400 BAD_REQUEST

    // 401 UNAUTHORIZED
    REJECTED_EXECUSION(UNAUTHORIZED, "수정 권한이 없습니다"),

    ACCESS_DENIED(UNAUTHORIZED, "유효하지 못한 토큰입니다."),

    EXPIRED_TOKEN(UNAUTHORIZED, "만료된 토큰입니다. 다시로그인하세요."),

    NOT_EXIST_REFRESH_TOKEN(UNAUTHORIZED, "리프레시토큰이 존재하지 않습니다."),

    NOT_EXIST_ACCESS_TOKEN(UNAUTHORIZED, "엑세스토큰이 존재하지 않슴니다."),

    // 403 FORBIDDEN
    YOUR_NOT_INVITED_BOARD(FORBIDDEN, "보드에 가입하지 않아서 권한이 없습니다."),

    YOUR_NOT_COME_IN(FORBIDDEN, "권한이 없습니다"), // 포괄적인 Forbidden 코드

    // 404 NOT_FOUND
    NOT_FOUND_MEMBER(NOT_FOUND, "해당 멤버를 찾을 수 없습니다"),

    NOT_FOUND_BOARD(NOT_FOUND, "해당 보드를 찾을 수 없습니다"),

    NOT_FOUND_CATALOG(NOT_FOUND, "해당 카탈로그를 찾을 수 없습니다"),

    NOT_FOUND_CARD(NOT_FOUND, "해당 카드를 찾을 수 없습니다"),

    NOT_FOUND_COMMENT(NOT_FOUND, "해당 댓글을 찾을 수 없습니다"),

    // 409 CONFLICT
    DUPLICATE_USERNAME(CONFLICT, "중복된 Username 입니다"),

    DUPLICATE_EMAIL(CONFLICT, "중복된 Email 입니다"),

    DUPLICATE_USERNAME_AND_EMAIL(CONFLICT, "중복된 Username 혹은 Email 입니다.")

    ;

    private final HttpStatus status;
    private final String msg;
    }
