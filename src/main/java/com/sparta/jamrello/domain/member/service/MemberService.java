package com.sparta.jamrello.domain.member.service;

import com.sparta.jamrello.domain.member.dto.EmailRequestDto;
import com.sparta.jamrello.domain.member.dto.SignupRequestDto;

public interface MemberService {

  void sendCodeToEmail(EmailRequestDto emailRequestDto);

  void signup(SignupRequestDto signupRequestDto);

}
