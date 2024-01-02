package com.sparta.jamrello.domain.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sparta.jamrello.domain.member.dto.DeleteMemberRequestDto;
import com.sparta.jamrello.domain.member.dto.EmailRequestDto;
import com.sparta.jamrello.domain.member.dto.MemberResponseDto;
import com.sparta.jamrello.domain.member.dto.SignupRequestDto;
import com.sparta.jamrello.domain.member.dto.UpdateMemberRequestDto;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.global.config.EmailConfig;
import com.sparta.jamrello.global.exception.BisException;
import com.sparta.jamrello.global.security.jwt.RefreshTokenRepository;
import com.sparta.jamrello.global.utils.EmailService;
import com.sparta.jamrello.global.utils.RedisService;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class MemberServiceImplIntegrationTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  RefreshTokenRepository refreshTokenRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  MemberServiceImpl memberService;

  @Autowired
  RedisService redisService;

  @Autowired
  EmailService emailService;

  @Autowired
  EmailConfig emailConfig;

  String username;
  String password;
  String nickname;
  String email;
  String authCode;
  UpdateMemberRequestDto updateMemberRequestDto;
  SignupRequestDto signupRequestDto;
  DeleteMemberRequestDto deleteMemberRequestDto;
  @BeforeEach
  void setup() {
    memberService = new MemberServiceImpl(memberRepository, passwordEncoder,
        refreshTokenRepository, emailService, redisService, emailConfig);
    username = "testUsername";
    password = "testPassword";
    nickname = "testNickname";
    email = "test@naver.com";
    authCode = "123456";

    signupRequestDto = new SignupRequestDto(username, password,
        nickname, email, authCode);

    updateMemberRequestDto = new UpdateMemberRequestDto(
        "updatePassword",
        "updateNickname",
        "updateEmail@naver.com"
    );

    deleteMemberRequestDto = new DeleteMemberRequestDto(password);

  }

  @AfterEach
   void after() {
    memberRepository.deleteAll();
    refreshTokenRepository.deleteAll();
  }

  @Nested
  @DisplayName("인증코드 Email 발송 테스트")
  class sendEmailTest {

    @Test
    @DisplayName("Email 발송 성공")
    void sendEmailTest_success () {
      // given
      EmailRequestDto emailRequestDto = new EmailRequestDto(email);

      // when
      memberService.sendCodeToEmail(emailRequestDto);
      String redisCode = redisService.getValues("AuthCode " + email);

      // then
      assertNotNull(redisCode);
    }

    @Test
    @DisplayName("Email 발송 실패 - Email 중복")
    void sendEmailTest_duplicationEmail () {
      // given
      EmailRequestDto emailRequestDto = new EmailRequestDto(email);
      memberRepository.save(Member.createMember(username, password, nickname, email));

      // when - then
      assertThrows(BisException.class,
          () -> memberService.sendCodeToEmail(emailRequestDto));
    }

  }

  @Nested
  @DisplayName("회원가입 테스트")
  class signupTest {

    @Test
    @DisplayName("회원가입 성공")
    void signupTest_success () {
      // given
      redisService.setValues("AuthCode " + email, authCode, Duration.ofMillis(300000));

      // when
      memberService.signup(signupRequestDto);

      // then
      assertNotNull(memberRepository);
      assertEquals(email, memberRepository.findByUsername(username).get().getEmail());
    }

    @Test
    @DisplayName("회원가입 실패 - authCode 불일치")
    void signupTest_NotMatchedAuthCode () {
      // given
      SignupRequestDto signupRequestDto2 = new SignupRequestDto(username, password, nickname, email, "av234c51");

      // when - then
      assertThrows(BisException.class,
          () -> memberService.signup(signupRequestDto2));
    }

    @Test
    @DisplayName("회원가입 실패 - username 중복")
    void signupTest_duplicationUsername () {
      // given
      memberRepository.save(Member.createMember(username, password, nickname, email));
      redisService.setValues("AuthCode " + email, authCode, Duration.ofMillis(300000));

      // when - then
      assertThrows(BisException.class,
          () -> memberService.signup(signupRequestDto));
    }
  }


  @Nested
  @DisplayName("내 정보 조회")
  class getMyProfile {

    @Test
    @DisplayName("내 정보 조회 성공")
    void getMtProfile_success() {
      // given
      Member testMember = memberRepository.save(Member.createMember(username, password, nickname, email));

      // when
      MemberResponseDto memberResponseDto = memberService.getProfile(testMember.getId(), testMember);

      // then
      assertNotNull(memberResponseDto);
      assertEquals(memberResponseDto.username(), username);
    }

    @Test
    @DisplayName("내 정보 조회 실패 - 권한 없음")
    void getMtProfile_NoAuthorization() {
      // given
      Member testMember1 = memberRepository.save(Member.createMember(username, password, nickname, email));
      Member testMember2 =  memberRepository.save(Member.createMember(
          "testname2",
          password,
          "testnickname2",
          "testEmail2@naver.com"
      ));

      // when - then
      assertThrows(BisException.class,
          () -> memberService.getProfile(testMember1.getId(), testMember2));
    }
  }

    @Nested
    @DisplayName("내 정보 수정")
    class updateMember {

      @Test
      @DisplayName("내 정보 수정 - 성공")
      void updateMember_success () {
        // given
        Member testMember = memberRepository.save(Member.createMember(username, password, nickname, email));

        // when
        MemberResponseDto memberResponseDto = memberService.updateMember(testMember.getId(), updateMemberRequestDto, testMember);

        // then
        assertNotNull(memberResponseDto);
        assertEquals(memberResponseDto.username(), username);
        assertEquals(memberResponseDto.nickname(), "updateNickname");
        assertEquals(memberResponseDto.email(), "updateEmail@naver.com");
      }

      @Test
      @DisplayName("내 정보 수정 - 권한 없음")
      void updateMember_NoAuthorization () {
        // given
        Member testMember1 = memberRepository.save(Member.createMember(username, password, nickname, email));
        Member testMember2 =  memberRepository.save(Member.createMember(
            "testname2",
            password,
            "testnickname2",
            "testEmail2@naver.com"
        ));

        // when - then
        assertThrows(BisException.class,
            () -> memberService.updateMember(testMember1.getId(), updateMemberRequestDto, testMember2));
      }

      @Test
      @DisplayName("내 정보 수정 - 존재하지 않은 멤버")
      void updateMember_NoExistMember () {
        // given
        memberRepository.save(Member.createMember(username, password, nickname, email));
        Member testMember2 =  memberRepository.save(Member.createMember(
            "testname2",
            password,
            "testnickname2",
            "testEmail2@naver.com"
        ));

        // when - then
        assertThrows(BisException.class,
            () -> memberService.updateMember(99L, updateMemberRequestDto, testMember2));
      }

      @Nested
      @DisplayName("멤버 탈퇴 테스트")
      class deleteMember {

        @Test
        @DisplayName("멤버 탈퇴 성공")
        void deleteMember_success () {
          // given
          String encodePassword = passwordEncoder.encode(password);
          Member testMember = memberRepository.save(Member.createMember(username,encodePassword,nickname,email));

          // when
          memberService.deleteMember(testMember.getId(),deleteMemberRequestDto,testMember);
          // then
          assertFalse(memberRepository.existsUserByUsername(username));

        }

        @Test
        @DisplayName("멤버 탈퇴 실패 - 패스워드 불일치")
        void deleteMember_NotMatchPassword () {
          // given
          String encodePassword = passwordEncoder.encode(password);
          Member testMember = memberRepository.save(Member.createMember(username,encodePassword,nickname,email));
          deleteMemberRequestDto = new DeleteMemberRequestDto("deletePassword");

          // when - then
          assertThrows(BisException.class,
              () -> memberService.deleteMember(testMember.getId(),deleteMemberRequestDto,testMember));

        }

        @Test
        @DisplayName("멤버 탈퇴 실패 - 권한 없음")
        void deleteMember_NoAuthorization () {
          // given
          String encodePassword = passwordEncoder.encode(password);
          Member testMember = memberRepository.save(Member.createMember(username,encodePassword,nickname,email));
          Member testMember2 = memberRepository.save(Member.createMember(
              "deleteMember", encodePassword,
              "deleteNickname",
              "delete@naver.com"
          ));

          // when - then
          assertThrows(BisException.class,
              () -> memberService.deleteMember(testMember.getId(),deleteMemberRequestDto,testMember2));

        }

      }
    }

}