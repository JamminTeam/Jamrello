package com.sparta.jamrello.domain.member.service;

import com.sparta.jamrello.domain.member.dto.DeleteMemberRequestDto;
import com.sparta.jamrello.domain.member.dto.EmailRequestDto;
import com.sparta.jamrello.domain.member.dto.MemberResponseDto;
import com.sparta.jamrello.domain.member.dto.SignupRequestDto;
import com.sparta.jamrello.domain.member.dto.UpdateMemberRequestDto;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.global.config.EmailConfig;
import com.sparta.jamrello.global.exception.BisException;
import com.sparta.jamrello.global.exception.ErrorCode;
import com.sparta.jamrello.global.security.jwt.RefreshTokenRepository;
import com.sparta.jamrello.global.utils.EmailService;
import com.sparta.jamrello.global.utils.RedisService;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private static final String AUTH_CODE_PREFIX = "AuthCode ";

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenRepository refreshTokenRepository;

    private final EmailService emailService;

    private final RedisService redisService;

  private final EmailConfig emailConfig;

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
            authCode, Duration.ofMillis(emailConfig.authCodeExpirationMillis));
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
    public MemberResponseDto getProfile(Long memberId, Member loginMember) {
        Member member = findUserInDBById(memberId);

        if (!member.getUsername().equals(loginMember.getUsername())) {
            throw new BisException(ErrorCode.YOUR_NOT_COME_IN);
        }

        return MemberResponseDto.buildMemberResponseDto(member);
    }

    @Override
    @Transactional
    public MemberResponseDto updateMember(
        Long memberId,
        UpdateMemberRequestDto updateMemberRequestDto,
        Member loginMember
    ) {
        Member member = findUserInDBById(memberId);

        if (!member.getUsername().equals(loginMember.getUsername())) {
            throw new BisException(ErrorCode.YOUR_NOT_COME_IN);
        }

        member.updateMember(updateMemberRequestDto,
            passwordEncoder.encode(updateMemberRequestDto.password()));
        return MemberResponseDto.buildMemberResponseDto(member);
    }

    @Override
    @Transactional
    public void deleteMember(
        Long memberId,
        DeleteMemberRequestDto deleteMemberRequestDto,
        Member loginMember
    ) {
        Member member = findUserInDBById(memberId);

        if (!member.getUsername().equals(loginMember.getUsername())) {
            throw new BisException(ErrorCode.YOUR_NOT_COME_IN);
        }

        if (!passwordEncoder.matches(deleteMemberRequestDto.password(), member.getPassword())) {
            throw new BisException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        memberRepository.delete(member);
        refreshTokenRepository.deleteByKeyUsername(member.getUsername());
    }

    public void sameMemberInDBByUsername(String username) {
        if (memberRepository.existsUserByUsername(username)) {
            throw new BisException(ErrorCode.DUPLICATE_MEMBER);
        }
    }

    public void sameMemberInDBByNickname(String nickname) {
        if (memberRepository.existsUserByNickname(nickname)) {
            throw new BisException(ErrorCode.DUPLICATE_USERNAME);
        }
    }

    public Member findUserInDBById(Long id) {
        return memberRepository.findById(id).orElseThrow(() ->
            new BisException(ErrorCode.NOT_FOUND_MEMBER)
        );
    }

    public void sameMemberInDBByEmail(String email) {
        if (memberRepository.existsUserByEmail(email)) {
            throw new BisException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    public void emailVerification(String email, String authCode) {

        //관리자용 테스트 인증번호 추후에 테스트완료 후 삭제 예정
        if (authCode.equals("777777")) {
            return;
        }

        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        if (!(redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode))) {
            throw new BisException(ErrorCode.NOT_MATCH_AUTHCODE);
        } else {
            redisService.deleteValues(redisAuthCode);
        }
    }

}
