package com.sparta.jamrello.domain.memberboard.entity;

import com.sparta.jamrello.domain.board.entity.Board;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "member_boards")
public class MemberBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberBoardRoleEnum role;

    public MemberBoard(Member member, Board board, MemberBoardRoleEnum memberBoardRoleEnum) {
        this.member = member;
        this.board = board;
        this.role = memberBoardRoleEnum;
    }

    public void updateRole(MemberBoardRoleEnum role) {
        this.role = role;
    }
}
