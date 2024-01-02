//package com.sparta.jamrello.domain.member.controller;
//
//import com.sparta.jamrello.JamrelloApplication;
//import com.sparta.jamrello.domain.member.service.MemberServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//@SpringBootTest(classes = JamrelloApplication.class)
//@AutoConfigureMockMvc
//class MemberControllerTest {
//  @Autowired
//  private MockMvc mockMvc;
//
//  @MockBean
//  private MemberServiceImpl memberService;
//
////  @Test
////  @DisplayName("Email 발송 성공 시")
////  public void authenticationEmail() {
////    //given
////    EmailRequestDto emailRequestDto = new EmailRequestDto("test@naver.com");
////
////    given(memberService.sendCodeToEmail(emailRequestDto)).willReturn();
////    //when
////
////    //then
////  }
//
//  @Test
//  void signupMember() {
//  }
//}