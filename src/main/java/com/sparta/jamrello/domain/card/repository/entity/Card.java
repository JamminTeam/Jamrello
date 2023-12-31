package com.sparta.jamrello.domain.card.repository.entity;

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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cards")
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

    @Setter
    private Long position;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startDay;

    @Column(updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dueDay;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardCollaborator> cardCollaboratorList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Builder
    public Card(String title, String description,Member member, Catalog catalog) {
        this.title = title;
        this.description = description;
        this.member = member;
        this.catalog = catalog;
        this.backgroundColor = "#ffffff";
        this.status = false;
        this.startDay = LocalDateTime.now();
        this.dueDay = LocalDateTime.now();
    }

    public void update(CardRequestDto requestDto) {
        this.title = requestDto.title();
        this.description = requestDto.description();
        this.backgroundColor = requestDto.backgroundColor();
    }

    public void updateCatalog(Catalog catalog) {
        this.catalog = catalog;
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
                    comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                )).toList(),
            card.getCardCollaboratorList().stream().map(
                cardCollaborator -> new CardCollaboratorResponseDto(
                    cardCollaborator.getMember().getNickname()
                )).toList()
        );
    }
}
