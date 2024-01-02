package com.sparta.jamrello.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 400 BAD_REQUEST
    ALREADY_EXIST_COLLABORATOR(BAD_REQUEST, "이미 존재하는 작업자입니다."),

    POSITION_OVER(BAD_REQUEST, "변경할 수 없는 위치입니다"),

    INVALID_VALUE(BAD_REQUEST, "잘못된 입력값입니다."),

    NOT_MATCH_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    NOT_MATCH_AUTHCODE(BAD_REQUEST, "인증번호가 일치하지 않습니다."),

    // 401 UNAUTHORIZED
    ACCESS_DENIED(UNAUTHORIZED, "유효하지 못한 토큰입니다."),

    LOGOUT_USER(UNAUTHORIZED, "로그아웃한 유저입니다. 다시 로그인해주세요."),

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

    NOT_FOUND_COLLABORATOR(NOT_FOUND, "해당 작업자를 찾을 수 없습니다"),

    // 409 CONFLICT
    DUPLICATE_MEMBER(CONFLICT, "이미 가입한 멤버 입니다."),

    DUPLICATE_USERNAME(CONFLICT, "중복된 Username 입니다"),

    DUPLICATE_EMAIL(CONFLICT, "중복된 Email 입니다"),

    DUPLICATE_USERNAME_AND_EMAIL(CONFLICT, "중복된 Username 혹은 Email 입니다."),

    // 그외 예외
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");;

    private final HttpStatus status;
    private final String msg;
}
