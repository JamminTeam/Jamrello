package com.sparta.jamrello.domain.comment.dto.response;

import com.sparta.jamrello.domain.member.repository.entity.Member;
import java.time.LocalDateTime;

public record CommentResponseDto(
    String content,
    LocalDateTime createdAt
) {

}
