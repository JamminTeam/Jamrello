package com.sparta.jamrello.domain.board.entity;

import com.sparta.jamrello.domain.board.dto.request.BoardRequestDto;
import com.sparta.jamrello.domain.catalog.repository.entity.Catalog;
import com.sparta.jamrello.domain.memberboard.entity.MemberBoard;
import com.sparta.jamrello.global.time.TimeStamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "boards")
public class Board extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String backgroundColor;

    @Column(name = "board_image")
    private String boardImageUrl;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Catalog> catalogList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemberBoard> memberBoardList = new ArrayList<>();


    @Builder
    public Board(String title, String backgroundColor) {
        this.title = title;
        this.backgroundColor = backgroundColor;
    }

    public static Board fromRequestDto(BoardRequestDto requestDto) {
        return Board.builder()
                .title(requestDto.title())
                .backgroundColor(requestDto.backgroundColor())
                .build();
    }

    public void updateBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void update(BoardRequestDto requestDto) {
        this.title = requestDto.title();
        this.backgroundColor = requestDto.backgroundColor();
    }

    public void updateBoardImageUrl(String fileUrl, String backgroundColor) {
        this.boardImageUrl = fileUrl;
        this.backgroundColor = backgroundColor;
    }

    public void removeBoardImageUrl() {
        this.boardImageUrl = null;
        this.backgroundColor = "#FFFFFF";
    }
}
