package com.sparta.jamrello.domain.comment.service;

import com.sparta.jamrello.domain.card.repository.CardRepository;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.comment.dto.CommentRequestDto;
import com.sparta.jamrello.domain.comment.repository.entity.Comment;
import com.sparta.jamrello.domain.comment.repository.CommentRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.domain.member.repository.entity.MemberRepository;
import com.sparta.jamrello.global.exception.BisException;
import com.sparta.jamrello.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;

    @Transactional
    public Comment createComment(Long memberId, Long cardId, CommentRequestDto commentRequestDto) {

        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isEmpty()) {
            throw new BisException(ErrorCode.NOT_FOUND_MEMBER);
        }

        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isEmpty()) {
            throw new BisException(ErrorCode.NOT_FOUND_CARD);
        }

        Comment comment = Comment.createCommentOf(commentRequestDto.content(), member.get(), card.get());
        commentRepository.save(comment);

        return comment;
    }
}
