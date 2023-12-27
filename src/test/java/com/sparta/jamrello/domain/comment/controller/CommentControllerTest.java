package com.sparta.jamrello.domain.comment.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.jamrello.JamrelloApplication;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.comment.dto.CommentRequestDto;
import com.sparta.jamrello.domain.comment.repository.entity.Comment;
import com.sparta.jamrello.domain.comment.service.CommentService.CommentService;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.global.dto.BaseResponse;
import com.sparta.jamrello.global.exception.BisException;
import com.sparta.jamrello.global.exception.ErrorCode;
import com.sparta.jamrello.global.security.UserDetailsImpl;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = JamrelloApplication.class)
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private Member testMember;
    private Card card;
    @BeforeEach
    public void setUp() {

        // Mock 테스트 UserDetails 생성
        testMember = new Member("testUser", "password", "nickname", "email@email.com");
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testMember);

        // SecurityContext 에 인증된 사용자 설정
        SecurityContextHolder.getContext()
            .setAuthentication(new UsernamePasswordAuthenticationToken(
                testUserDetails, testUserDetails.getPassword(), testUserDetails.getAuthorities()));

        card = Mockito.mock(Card.class);
        when(card.getId()).thenReturn(1L);
    }

    @Test
    @DisplayName("Comment Controller 성공시")
    public void createCommentTest() throws Exception {

        CommentRequestDto commentRequestDto = new CommentRequestDto("test comment");
        Comment comment = commentRequestDto.toEntity(testMember, card);

        given(commentService.createComment(testMember.getId(), card.getId(), commentRequestDto))
            .willReturn(comment);

        // When
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/cards/{cardId}/comment", 1L)
                    .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                    .content(new ObjectMapper().writeValueAsString(commentRequestDto))
                    .characterEncoding("UTF-8")

            )
            .andExpect(MockMvcResultMatchers.status().is(200))
            .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();

        String responseString = mvcResult.getResponse().getContentAsString();
        BaseResponse baseResponse = objectMapper.readValue(responseString, BaseResponse.class);

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals("댓글이 생성되었습니다", baseResponse.getMsg());
        Assertions.assertNotNull(baseResponse.getData());

    }

}