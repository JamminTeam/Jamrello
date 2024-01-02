package com.sparta.jamrello.global.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

  /* 200 OK */
  /* uSER */
  OK(200, "요청 성공"),
  SEND_MAIL(200, "이메일이 발송되었습니다. 이메일을 확인해주세요."),
  LOGIN(200, "로그인 성공"),
  SUCCESS_REISSUANCETOKEN(200, "토큰이 재발급되었습니다. 다시 시도해주세요."),
  LOGOUT(200, "로그아웃 되었습니다."),
  GET_MY_PROFILE(200, "회원정보 가져오기 성공"),
  UPDATE_MY_PROFILE(200, "회원정보 수정 완료"),
  WITHDRAW_MEMBER(200, "회원탈퇴 완료"),

  /* BOARD */
  UPDATE_BOARD(200, "보드 수정 완료"),
  DELETE_BOARD(200, "보드 삭제 완료"),
  INVITE_MEMBER(200, "멤버 초대 성공"),
  GET_BOARD(200, "보드 가져오기"),
  UPDATE_IMAMGE(200, "배경이미지 수정 완료"),
  DELETE_IMAMGE(200, "배경이미지 삭제 완료"),

  /* CATALOG */
  UPDATE_CATALOG(200, "카탈로그 수정 완료"),
  KEEP_CATALOG(200, "카탈로그 보관 완료"),
  DELETE_CATALOG(200, "카탈로그 삭제 완료"),
  MOVE_CATALOG_POSITION(200, "카탈로그 순서 이동 완료"),

  /* CARD */
  UPDATE_CARD(200, "카드 수정 완료"),
  DELETE_CARD(200, "카드 삭제 완료"),
  GET_CARD_CONTENT(200, "카드 조회 성공"),
  MOVE_CARD_POSITION(200, "카드 순서 이동 완료"),
  ADD_USER(200, "멤버 추가"),
  DELETE_USER(200, "멤버 삭제 성공"),
  SETUP_DURATION(200, "기간 설정 완료"),

  /* COMMENT */
  UPDATE_COMMENT(200, "댓글 수정 완료"),
  DELETE_COMMENT(200, "댓글 삭제 완료"),
  GET_COMMENT_CONTENT(200, "댓글 조회 성공"),
  UPDATE_COMMENT_IMAMGE(200, "댓글 이미지 수정 완료"),
  DELETE_COMMENT_IMAMGE(200, "댓글 이미지 삭제 완료"),



  /* 201 CREATED */
  SIGNUP(201, "회원가입 성공"),
  CREATED_BOARD(201, "보드 생성 성공"),
  CREATED_CATALOG(201, "카탈로그 생성 성공"),
  CREATED_CARD(201, "카드 생성 성공"),
  CREATED_COMMENT(201, "댓글 생성 성공");

  private final int httpStatus;
  private final String message;

}
