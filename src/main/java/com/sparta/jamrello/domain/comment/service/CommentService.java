package com.sparta.jamrello.domain.comment.service;

import com.sparta.jamrello.domain.card.repository.CardRepository;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.comment.dto.CommentRequestDto;
import com.sparta.jamrello.domain.comment.dto.CommentResponseDto;
import com.sparta.jamrello.domain.comment.repository.entity.Comment;
import com.sparta.jamrello.domain.comment.repository.CommentRepository;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.global.exception.BisException;
import com.sparta.jamrello.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;

    @Transactional
    public Comment createComment(Long memberId, Long cardId, CommentRequestDto commentRequestDto) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_MEMBER));

        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_CARD));

        Comment comment = Comment.createCommentOf(commentRequestDto.content(), member, card);
        commentRepository.save(comment);

        return comment;
    }

    @Transactional
    public Comment updateComment(Long commentId, Member member,
        CommentRequestDto commentRequestDto) {

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_COMMENT));

        if (isAuthorizedMember(member, comment)) {
            throw new BisException(ErrorCode.ACCESS_DENIED);
        }

        comment.updateComment(commentRequestDto);
        commentRepository.save(comment);
        return comment;
    }

    @Transactional
    public void deleteComment(Long commentId, Member member) {

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_COMMENT));

        if (isAuthorizedMember(member, comment)) {
            throw new BisException(ErrorCode.ACCESS_DENIED);
        }

        commentRepository.delete(comment);
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_COMMENT));
    }

    public Member getMemberByCommentId(Long commentId) {
        return commentRepository.findMemberByCommentId(commentId)
            .orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_MEMBER));
    }

    public List<CommentResponseDto> getComments(Pageable pageable) {
        List<Comment> commentList = commentRepository.findAllCommentsWithPagination(pageable).getContent();

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResponseDtoList.add(Comment.toCommentResponse(comment.getMember(), comment));
        }
        return commentResponseDtoList;
    }


    private boolean isAuthorizedMember(Member member, Comment comment) {
        return comment.getMember().getUsername().equals(member.getUsername());
    }
}
