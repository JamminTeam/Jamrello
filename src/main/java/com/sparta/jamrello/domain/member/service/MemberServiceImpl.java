package com.sparta.jamrello.domain.member.service;

import com.sparta.jamrello.domain.member.dto.EmailRequestDto;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.global.utils.EmailService;
import com.sparta.jamrello.global.utils.RedisService;
import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService{

  private static final String AUTH_CODE_PREFIX = "AuthCode ";

  private final MemberRepository memberRepository;

  private final EmailService emailService;

  private final RedisService redisService;

//  // 만료시간 5분
//  @Value("${spring.mail.auth-code-expiration-millis}")
//  private Long authCodeExpirationMillis;

  @Override
  public void sendCodeToEmail(EmailRequestDto emailRequestDto) {
    String email = emailRequestDto.email();

    // email 중복검사
    sameUserInDBByEmail(email);

    String title = "회원가입 이메일 인증 번호";
    String authCode = emailService.createCode();
    emailService.sendEmail(email, title, authCode);

    // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
    redisService.setValues(AUTH_CODE_PREFIX + email,
        authCode, Duration.ofMillis(300000));
  }

  public void sameUserInDBByEmail(String email) {
    if (memberRepository.existsUserByEmail(email)) {
      throw new IllegalArgumentException("이미 가입된 이메일이 존재합니다.");
    }
  }


}
