package com.sparta.jamrello.domain.member.service;

import com.sparta.jamrello.domain.member.dto.EmailRequestDto;

public interface MemberService {

  void sendCodeToEmail(EmailRequestDto emailRequestDto);

}
