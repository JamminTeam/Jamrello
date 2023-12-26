package com.sprta.jamrello.domain.card.repository.entity;

import com.sprta.jamrello.domain.cardCollaborators.repository.entity.CardCollaborator;
import com.sprta.jamrello.domain.catalog.repository.entity.Catalog;
import com.sprta.jamrello.domain.comment.repository.Comment;
import com.sprta.jamrello.global.time.TimeStamp;
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
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cards")
public class Card extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id", referencedColumnName = "id")
    private Catalog catalog;

    @Column(nullable = false)
    private String title;

    private String description;

    private String imageUrl;

    @ColumnDefault("#ffffff")
    private String backgroundColor;

    @ColumnDefault("false")
    private boolean status;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startDay;

    @Column(updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dueDay;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "cardcollaborator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardCollaborator> cardCollaborators = new ArrayList<>();

    @Builder
    public Card(String title, String description) {
        this.title = title;
        this.description = description;
    }

}
