package com.sparta.jamrello.domain.comment.dto;

import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.comment.repository.entity.Comment;
import com.sparta.jamrello.domain.member.repository.entity.Member;

public record CommentRequestDto(
    String content
) {

}
