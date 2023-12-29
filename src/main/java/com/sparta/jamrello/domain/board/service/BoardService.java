package com.sparta.jamrello.domain.board.service;

import com.sparta.jamrello.domain.board.dto.request.BoardRequestDto;
import com.sparta.jamrello.domain.board.dto.request.InviteMemberDto;
import com.sparta.jamrello.domain.board.dto.response.BoardListResponseDto;
import com.sparta.jamrello.domain.board.dto.response.BoardResponseDto;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import java.util.List;

public interface BoardService {

  /**
   * Board 생성
   *
   * @param requestDto 사용자가 입력한 데이터
   * @param authMember
   * @return BoardResponseDto
   */
  BoardResponseDto createBoard(BoardRequestDto requestDto, Member authMember);


  /**
   * Board 수정
   *
   * @param boardId    수정할 보드 ID
   * @param requestDto 수정 내역
   * @param authMember
   */
  BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto, Long authMember);

  /**
   * Board 삭제
   *
   * @param boardId 삭제할 보드 ID
   * @param id
   */
  void deleteBoard(Long boardId, Long id);

  /**
   * Board에 멤버 초대
   *
   * @param boardId         초대할 보드 ID
   * @param inviteMemberDto 초대할 멤버 email
   */
  void inviteMember(Long boardId, InviteMemberDto inviteMemberDto);


  /**
   * 특정 보드에 있는 카탈로그 전체 조회
   *
   * @param boardId 조회할 보드 ID
   */
  List<BoardListResponseDto> getBoard(Long boardId);
}
