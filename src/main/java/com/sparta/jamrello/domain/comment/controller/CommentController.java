package com.sparta.jamrello.domain.comment.controller;

import com.sparta.jamrello.domain.comment.dto.CommentRequestDto;
import com.sparta.jamrello.domain.comment.repository.entity.Comment;
import com.sparta.jamrello.domain.comment.service.CommentService.CommentService;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.global.constant.ResponseCode;
import com.sparta.jamrello.global.dto.BaseResponse;
import com.sparta.jamrello.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/cards/{cardId}/comment")
    public ResponseEntity<BaseResponse<Comment>> createComment(
        @RequestBody CommentRequestDto commentRequestDto,
//        @AuthUser Member member
        @PathVariable Long cardId) {

//        Member member = userDetails.getMember();
        Member member = new Member("testUser", "password", "nickname", "email@email.com");

        Comment commemt = commentService.createComment(member.getId(), cardId, commentRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.of(ResponseCode.CREATED_COMMENT, commemt));
    }


}
