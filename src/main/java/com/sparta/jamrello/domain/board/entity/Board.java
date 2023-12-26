package com.sparta.jamrello.domain.board.entity;

import com.sparta.jamrello.global.entity.MemberBoard;
import com.sparta.jamrello.domain.board.dto.request.BoardRequestDto;
import com.sparta.jamrello.domain.catalog.repository.entity.Catalog;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "boards")
public class Board extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    @ColumnDefault("'#ffffff'")
    private String backgroundColor;

    @ColumnDefault("false")
    private boolean status;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Catalog> catalogList = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<MemberBoard> memberBoardList = new ArrayList<>();


    @Builder
    public Board(String title, String username, String backgroundColor) {
        this.title = title;
        this.username = username;
        this.backgroundColor = backgroundColor;
    }

    public static Board fromRequestDto(BoardRequestDto requestDto) {
        return Board.builder()
                .title(requestDto.title())
                .username(requestDto.username())
                .backgroundColor(requestDto.backgroundColor())
                .build();
    }

}
