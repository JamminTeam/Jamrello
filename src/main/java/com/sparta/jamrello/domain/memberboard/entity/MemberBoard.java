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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_boards")
@NoArgsConstructor
@Getter
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
  private com.sparta.jamrello.domain.memberboard.entity.MemberBoardRoleEnum role;

  @Builder
  public MemberBoard(Member member, Board board, com.sparta.jamrello.domain.memberboard.entity.MemberBoardRoleEnum memberBoardRoleEnum) {
    this.member = member;
    this.board = board;
    this.role = memberBoardRoleEnum;
  }

  public static MemberBoard createMemberBoard(Member member, Board board, com.sparta.jamrello.domain.memberboard.entity.MemberBoardRoleEnum memberBoardRoleEnum) {
    return MemberBoard.builder()
        .member(member)
        .board(board)
        .memberBoardRoleEnum(memberBoardRoleEnum)
        .build();
  }

  public void updateRole(com.sparta.jamrello.domain.memberboard.entity.MemberBoardRoleEnum role) {
    this.role = role;
  }
}