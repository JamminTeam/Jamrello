package com.sparta.jamrello.domain.member.dto;

import com.sparta.jamrello.domain.member.repository.entity.Member;
import lombok.Builder;


public record MemberResponseDto (
    Long id,
    String username,
    String nickname,
    String email

) {

  @Builder
  public MemberResponseDto(Long id, String username, String nickname, String email) {
    this.id = id;
    this.username = username;
    this.nickname = nickname;
    this.email = email;
  }

  public static MemberResponseDto buildMemberResponseDto(Member member) {
    return MemberResponseDto.builder()
        .id(member.getId())
        .username(member.getUsername())
        .nickname(member.getNickname())
        .email(member.getEmail())
        .build();

  }
}
