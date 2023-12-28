package com.sparta.jamrello.domain.comment.service;

import com.sparta.jamrello.domain.comment.dto.CommentRequestDto;
import com.sparta.jamrello.domain.comment.dto.CommentResponseDto;
import com.sparta.jamrello.domain.comment.repository.entity.Comment;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Comment createComment(Long memberId, Long cardId, CommentRequestDto commentRequestDto);

    Comment updateComment(Long commentId, Member member,
        CommentRequestDto commentRequestDto);

    void deleteComment(Long commentId, Member member);

    Comment getComment(Long commentId);

    Member getMemberByCommentId(Long commentId);

    List<CommentResponseDto> getComments(Pageable pageable);

}
