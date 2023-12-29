package com.sparta.jamrello.domain.catalog.repository.entity;

import com.sparta.jamrello.domain.board.entity.Board;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.catalog.dto.CatalogRequestDto;
import com.sparta.jamrello.domain.catalog.dto.CatalogResponseDto;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private boolean status;

    @Column(nullable = false)
    private Long position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cardList = new ArrayList<>();

    @Builder
    public Catalog(String title, Long position, Board board, boolean status) {
        this.title = title;
        this.position = position;
        this.board = board;
        this.status = status;
    }

    public static Catalog createCatalog(Board board, CatalogRequestDto requestDto, Long position) {
        return Catalog.builder()
            .board(board)
            .title(requestDto.title())
            .position(position)
            .status(false)
            .build();
    }

    public void addCatalogInBoard() {
        this.board.getCatalogList().add(this);
    }

    public static CatalogResponseDto createCatalogResponseDto(Catalog catalog) {
        return new CatalogResponseDto(
            catalog.getId(),
            catalog.title,
            catalog.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            catalog.position
        );
    }

    public void updateCatalogTitle(CatalogRequestDto requestDto) {
        this.title = requestDto.title();
    }

    public void changeStatus() {
        this.status = !this.status;
    }
}
