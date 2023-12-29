package com.sparta.jamrello.domain.comment.service;

import com.sparta.jamrello.domain.comment.dto.request.CommentRequestDto;
import com.sparta.jamrello.domain.comment.dto.response.CommentResponseDto;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentResponseDto createComment(Long memberId, Long cardId,
        CommentRequestDto commentRequestDto);

    CommentResponseDto updateComment(Long commentId, Long memberId,
        CommentRequestDto commentRequestDto);

    void deleteComment(Long commentId, Long memberId);

    CommentResponseDto getComment(Long commentId);

    Member getMemberByCommentId(Long commentId);

    List<CommentResponseDto> getComments(Pageable pageable);

}
