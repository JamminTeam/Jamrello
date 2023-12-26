package com.sprta.jamrello.domain.catalog.repository.entity;

import com.sprta.jamrello.domain.board.entity.Board;
import com.sprta.jamrello.domain.card.repository.entity.Card;
import com.sprta.jamrello.domain.catalog.dto.CatalogRequestDto;
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
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "catalogs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Catalog extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ColumnDefault("false")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "column", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards;

    @Builder
    public Catalog(String title, Board board) {
        this.title = title;
        this.board = board;
    }

    public static Catalog createCatalog(Board board, CatalogRequestDto requestDto) {
        return Catalog.builder()
                .board(board)
                .title(requestDto.title())
                .build();
    }
}
