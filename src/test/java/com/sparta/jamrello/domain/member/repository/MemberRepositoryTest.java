package com.sparta.jamrello.domain.member.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sparta.jamrello.domain.member.dto.UpdateMemberRequestDto;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class MemberRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  @Mock
  PasswordEncoder passwordEncoder;

  private Member member;

  @BeforeEach
  void setup() {
    member = Member.createMember("testmember", "testpassword", "testnickname", "test@naver.com");
  }


  @Test
  @DisplayName("멤버 저장 테스트")
  void meberSaveTest_success() {
    // given - when
    Member resultmember = memberRepository.save(member);

    // then
    assertNotNull(resultmember);
    assertEquals(member.getUsername(), resultmember.getUsername());
    assertEquals(member.getNickname(), resultmember.getNickname());
    assertEquals(member.getPassword(), resultmember.getPassword());
    assertEquals(member.getEmail(), resultmember.getEmail());
  }

  @Test
  @DisplayName("멤버 수정 테스트 - nickname 수정")
  void memberUpdateTest_success() {
    // given
    Member saveMember = memberRepository.save(member);
    UpdateMemberRequestDto updateMemberRequestDto = new UpdateMemberRequestDto("testpassword", "updatename", "test@naver.com");
    String encodePassword = passwordEncoder.encode(updateMemberRequestDto.password());
    saveMember.updateMember(updateMemberRequestDto, encodePassword);
    // when
    Member updateMember = memberRepository.save(saveMember);
    // then
    assertNotNull(updateMember);
    assertNotEquals("testnickname", updateMember.getNickname());
    assertFalse(passwordEncoder.matches("testpassword", updateMember.getPassword()));
    assertEquals("test@naver.com", updateMember.getEmail());

  }

  @Test
  @DisplayName("멤버 삭제 테스트")
  void memberDeleteTest_success() {
    // given
    Member saveMember = memberRepository.save(member);
    // when
    memberRepository.delete(saveMember);
    // then
    assertEquals(Optional.empty(), memberRepository.findByUsername(saveMember.getUsername()));
  }

}