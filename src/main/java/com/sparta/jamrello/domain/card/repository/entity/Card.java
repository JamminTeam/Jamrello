package com.sparta.jamrello.domain.card.repository.entity;

import com.sparta.jamrello.domain.card.dto.request.CardDuedateRequestDto;
import com.sparta.jamrello.domain.card.dto.request.CardRequestDto;
import com.sparta.jamrello.domain.card.dto.response.CardResponseDto;
import com.sparta.jamrello.domain.cardCollaborators.dto.CardCollaboratorResponseDto;
import com.sparta.jamrello.domain.cardCollaborators.repository.entity.CardCollaborator;
import com.sparta.jamrello.domain.catalog.repository.entity.Catalog;
import com.sparta.jamrello.domain.comment.dto.response.CommentResponseDto;
import com.sparta.jamrello.domain.comment.repository.entity.Comment;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.global.time.TimeStamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Card extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    @Column(nullable = false)
    private String title;

    private String description;

    private String imageUrl;

    @Column(nullable = false)
    private String backgroundColor;

    private boolean status;

    private Long position;

    private LocalDateTime startDay;

    private LocalDateTime dueDay;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardCollaborator> cardCollaboratorList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Builder
    public Card(String title, String description, String backgroundColor, Member member,
        Catalog catalog) {
        this.title = title;
        this.description = description;
        this.member = member;
        this.catalog = catalog;
        this.backgroundColor = backgroundColor;
        this.status = false;
        this.startDay = LocalDateTime.now();
        this.dueDay = LocalDateTime.now().plusDays(1);
    }

    public static Card createCard(CardRequestDto requestDto, Member member, Catalog catalog) {
        return Card.builder()
            .title(requestDto.title())
            .description(requestDto.description())
            .backgroundColor(requestDto.backgroundColor())
            .member(member)
            .catalog(catalog)
            .build();
    }

    public void updatePosition(Long position) {
        this.position = position;
    }

    public void update(CardRequestDto requestDto) {
        this.title = requestDto.title();
        this.description = requestDto.description();
        this.backgroundColor = requestDto.backgroundColor();
    }

    public void updateCardDueDay(CardDuedateRequestDto requestDto) {
        this.startDay = requestDto.startDay();
        this.dueDay = requestDto.dueDay();
    }

    public void updateCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public void updateStatus() {
        this.status = true;
    }

    public CardResponseDto createResponseDto(Card card) {
        return new CardResponseDto(
            card.getId(),
            card.getTitle(),
            card.getMember().getNickname(),
            card.getDescription(),
            card.getBackgroundColor(),
            card.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            card.getStartDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            card.getDueDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            card.getCommentList().stream().map(
                comment -> new CommentResponseDto(
                    comment.getMember().getNickname(),
                    comment.getContent(),
                    comment.getCreatedAt()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                )).toList(),
            card.getCardCollaboratorList().stream().map(
                cardCollaborator -> new CardCollaboratorResponseDto(
                    cardCollaborator.getMember().getNickname()
                )).toList()
        );
    }

    public void updateBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
