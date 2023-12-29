package com.sparta.jamrello.domain.catalog.repository.entity;

import com.sparta.jamrello.domain.board.entity.Board;
import com.sparta.jamrello.domain.catalog.dto.CatalogRequestDto;
import com.sparta.jamrello.global.time.TimeStamp;
import com.sparta.jamrello.domain.card.repository.entity.Card;
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

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cardList;

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
