package com.sparta.jamrello.domain.card.dto.response;

import com.sparta.jamrello.domain.cardCollaborators.dto.CardCollaboratorResponseDto;
import com.sparta.jamrello.domain.comment.dto.response.CommentResponseDto;
import java.util.List;

public record CardResponseDto(

        Long id,
        String title,
        String nickname,
        String description,
        String backgroundColor,
        String modifiedAt,
        String startDay,
        String dueDay,
        List<CommentResponseDto> commentList,
        List<CardCollaboratorResponseDto> cardCollaboratorList
) {

}
