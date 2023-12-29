package com.sparta.jamrello.domain.member.service;

import com.sparta.jamrello.domain.member.dto.DeleteMemberRequestDto;
import com.sparta.jamrello.domain.member.dto.EmailRequestDto;
import com.sparta.jamrello.domain.member.dto.MemberResponseDto;
import com.sparta.jamrello.domain.member.dto.SignupRequestDto;
import com.sparta.jamrello.domain.member.dto.UpdateMemberRequestDto;
import com.sparta.jamrello.domain.member.entity.Member;

public interface MemberService {

  void sendCodeToEmail(EmailRequestDto emailRequestDto);

  void signup(SignupRequestDto signupRequestDto);

  MemberResponseDto getProfile(Long memberId, Member userDetails);

  MemberResponseDto updateMember(Long memberId, UpdateMemberRequestDto updateMemberRequestDto,
      Member userDetails);

  void deleteMember(Long memberId, DeleteMemberRequestDto deleteMemberRequestDto, Member loginMember);

}
