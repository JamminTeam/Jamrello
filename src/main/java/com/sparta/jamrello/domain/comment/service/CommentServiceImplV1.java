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
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImplV1 implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;

    @Override
    public Comment createComment(Long memberId, Long cardId, CommentRequestDto commentRequestDto) {

        Member member = findMember(memberId);
        Card card = findCard(cardId);

        Comment comment = Comment.createCommentOf(commentRequestDto.content(), member, card);
        commentRepository.save(comment);

        return comment;
    }

    @Override
    @Transactional
    public Comment updateComment(Long commentId, Long memberId,
        CommentRequestDto commentRequestDto) {

        Member member = findMember(memberId);
        Comment comment = findComment(commentId);

        if (!isAuthorizedMember(member, comment)) {
            throw new BisException(ErrorCode.ACCESS_DENIED);
        }

        comment.updateComment(commentRequestDto);
        commentRepository.save(comment);
        return comment;
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long memberId) {

        Member member = findMember(memberId);
        Comment comment = findComment(commentId);

        if (!isAuthorizedMember(member, comment)) {
            throw new BisException(ErrorCode.ACCESS_DENIED);
        }

        commentRepository.delete(comment);
    }

    @Override
    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_COMMENT));
    }

    @Override
    public Member getMemberByCommentId(Long commentId) {
        return commentRepository.findMemberByCommentId(commentId)
            .orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_MEMBER));
    }

    @Override
    public List<CommentResponseDto> getComments(Pageable pageable) {
        List<Comment> commentList = commentRepository.findAllCommentsWithPagination(pageable)
            .getContent();

        List<CommentResponseDto> commentResponseDtoList = commentList.stream()
            .map(comment -> Comment.toCommentResponse(comment.getMember(), comment))
            .collect(Collectors.toList());

        return commentResponseDtoList;
    }


    private boolean isAuthorizedMember(Member member, Comment comment) {
        return comment.getMember().getUsername().equals(member.getUsername());
    }

    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_COMMENT));
    }

    private Card findCard(Long cardId) {
        return cardRepository.findById(cardId)
            .orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_CARD));
    }
}
