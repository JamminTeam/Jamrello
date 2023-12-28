package com.sparta.jamrello.domain.member.service;

import com.sparta.jamrello.domain.member.dto.EmailRequestDto;
import com.sparta.jamrello.domain.member.dto.MemberResponseDto;
import com.sparta.jamrello.domain.member.dto.SignupRequestDto;
import com.sparta.jamrello.domain.member.dto.UpdateMemberRequestDto;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.global.security.UserDetailsImpl;
import com.sparta.jamrello.global.utils.EmailService;
import com.sparta.jamrello.global.utils.RedisService;
import java.time.Duration;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService{

  private static final String AUTH_CODE_PREFIX = "AuthCode ";

  private final MemberRepository memberRepository;

  private final PasswordEncoder passwordEncoder;

  private final EmailService emailService;

  private final RedisService redisService;

//  // 만료시간 5분
//  @Value("${spring.mail.auth-code-expiration-millis}")
//  private Long authCodeExpirationMillis;

  @Override
  public void sendCodeToEmail(EmailRequestDto emailRequestDto) {
    String email = emailRequestDto.email();

    // email 중복검사
    sameMemberInDBByEmail(email);

    String title = "회원가입 이메일 인증 번호";
    String authCode = emailService.createCode();
    emailService.sendEmail(email, title, authCode);

    // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
    redisService.setValues(AUTH_CODE_PREFIX + email,
        authCode, Duration.ofMillis(300000));
  }

  @Override
  public void signup(SignupRequestDto signupRequestDto) {
    String username = signupRequestDto.username();
    String nickname = signupRequestDto.nickname();
    String email = signupRequestDto.email();
    String authCode = signupRequestDto.code();

    // 이메일 인증검사
    emailVerification(email, authCode);

    // 유저네임 중복검사
    sameMemberInDBByUsername(username);

    // 닉네임 중복검사
    sameMemberInDBByNickname(nickname);

    // 비밀번호 암호화
    String password = passwordEncoder.encode(signupRequestDto.password());

    // 새 멤버 등록
    Member member = Member.createMember(username, password, nickname, email);
    memberRepository.save(member);
  }

  @Override
  public MemberResponseDto getProfile(Long memberId, UserDetailsImpl userDetails) {
    Member member = findUserInDBById(memberId);

    if (!member.getUsername().equals(userDetails.getMember().getUsername())) {
      throw new IllegalArgumentException("자신의 정보만 조회 할 수 있습니다.");
    }

    return MemberResponseDto.buildMemberResponseDto(member);
  }

  @Override
  @Transactional
  public MemberResponseDto updateMember(
      Long memberId,
      UpdateMemberRequestDto updateMemberRequestDto,
      UserDetailsImpl userDetails
  ) {
    Member member = findUserInDBById(memberId);

    if (!member.getUsername().equals(userDetails.getMember().getUsername())) {
      throw new IllegalArgumentException("자신의 정보만 수정 할 수 있습니다.");
    }

    member.updateMember(updateMemberRequestDto);
    return MemberResponseDto.buildMemberResponseDto(member);
  }

  public void sameMemberInDBByUsername(String username) {
    if (memberRepository.existsUserByUsername(username)) {
      throw new IllegalArgumentException("이미 가입한 유저입니다.");
    }
  }
  public void sameMemberInDBByNickname(String nickname) {
    if (memberRepository.existsUserByNickname(nickname)) {
      throw new IllegalArgumentException("중복된 닉네임입니다.");
    }
  }

  public Member findUserInDBById(Long id) {
    Member member = memberRepository.findById(id).orElseThrow(() ->
        new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
    );
    return member;
  }

  public void sameMemberInDBByEmail(String email) {
    if (memberRepository.existsUserByEmail(email)) {
      throw new IllegalArgumentException("이미 가입된 이메일이 존재합니다.");
    }
  }

  private void emailVerification(String email, String authCode) {
    String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
    if (!(redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode))) {
      throw new IllegalArgumentException("인증번호가 틀렸습니다. 다시 입력해주세요.");
    } else {
      redisService.deleteValues(redisAuthCode);
    }
  }

}
