package com.sparta.jamrello.domain.comment.controller;

import com.sparta.jamrello.domain.comment.dto.CommentRequestDto;
import com.sparta.jamrello.domain.comment.dto.CommentResponseDto;
import com.sparta.jamrello.domain.comment.repository.entity.Comment;
import com.sparta.jamrello.domain.comment.service.CommentService;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.global.annotation.AuthUser;
import com.sparta.jamrello.global.constant.ResponseCode;
import com.sparta.jamrello.global.dto.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public ResponseEntity<BaseResponse<CommentResponseDto>> createComment(
        @RequestBody CommentRequestDto commentRequestDto,
//        @AuthUser Member member
        @PathVariable Long cardId) {

//        Member member = member.getMember();
        Member member = new Member("testUser", "password", "nickname", "email@email.com");

        CommentResponseDto commentResponseDto = Comment.toCommentResponse(member,
            commentService.createComment(member.getId(), cardId, commentRequestDto));

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(BaseResponse.of(ResponseCode.CREATED_COMMENT, commentResponseDto));
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<CommentResponseDto>> updateComment(
        @RequestBody CommentRequestDto commentRequestDto,
        @AuthUser Member member,
        @PathVariable Long commentId) {

        CommentResponseDto commentResponseDto = Comment.toCommentResponse(member,
            commentService.updateComment(commentId, member.getId(), commentRequestDto));

        return ResponseEntity.status(HttpStatus.OK)
            .body(BaseResponse.of(ResponseCode.UPDATE_COMMENT, commentResponseDto));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<CommentResponseDto>> deleteComment(
        @AuthUser Member member,
        @PathVariable Long commentId) {

        commentService.deleteComment(commentId, member.getId());
        return ResponseEntity.status(HttpStatus.OK)
            .body(BaseResponse.of(ResponseCode.DELETE_COMMENT, null));
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<CommentResponseDto>> getComment(
        @AuthUser Member member,
        @PathVariable Long commentId) {

        CommentResponseDto commentResponseDto = Comment.toCommentResponse(member, commentService.getComment(commentId));

        return ResponseEntity.status(HttpStatus.OK)
            .body(BaseResponse.of(ResponseCode.GET_COMMENT_CONTENT, commentResponseDto));
    }

    @GetMapping("/comments")
    public ResponseEntity<BaseResponse<List<CommentResponseDto>>> getCommentList(
        Pageable pageable
    ) {
        List<CommentResponseDto> comments = commentService.getComments(pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(BaseResponse.of(ResponseCode.GET_COMMENT_CONTENT, comments));
    }



}
