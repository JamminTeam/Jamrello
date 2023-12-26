package com.sparta.jamrello.domain.comment.dto;

import com.sparta.jamrello.domain.comment.repository.Comment;

public record CommentRequestDto(
        String content
) {
    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }
}
