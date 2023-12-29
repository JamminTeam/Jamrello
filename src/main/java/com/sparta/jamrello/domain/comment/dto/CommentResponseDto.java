package com.sparta.jamrello.domain.comment.dto;

import com.sparta.jamrello.domain.member.entity.Member;
import java.time.LocalDateTime;

public record CommentResponseDto(
    Member member,
    String content,
    LocalDateTime createdAt
) {

}
