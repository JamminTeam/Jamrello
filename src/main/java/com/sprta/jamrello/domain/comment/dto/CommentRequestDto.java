package com.sprta.jamrello.domain.comment.dto;

import com.sprta.jamrello.domain.comment.repository.Comment;

public record CommentRequestDto(
        String content
) {
    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .build();
    }
}
