package com.sparta.jamrello.domain.board.controller;

import com.sparta.jamrello.domain.board.dto.request.BoardRequestDto;
import com.sparta.jamrello.domain.board.dto.request.InviteMemberDto;
import com.sparta.jamrello.domain.board.dto.response.BoardListResponseDto;
import com.sparta.jamrello.domain.board.dto.response.BoardResponseDto;
import com.sparta.jamrello.domain.board.service.BoardService;
import com.sparta.jamrello.domain.board.service.BoardServiceImplV1;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.domain.memberBoard.entity.MemberBoardRoleEnum;
import com.sparta.jamrello.global.annotation.AuthUser;
import com.sparta.jamrello.global.constant.ResponseCode;
import com.sparta.jamrello.global.dto.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

  private final BoardService boardService;

  @PostMapping
  public ResponseEntity<BaseResponse<BoardResponseDto>> createBoard(
      @RequestBody BoardRequestDto requestDto, @AuthUser Member authMember) {

    BoardResponseDto responseDto = boardService.createBoard(requestDto, authMember,
        MemberBoardRoleEnum.ADMIN);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.of(ResponseCode.CREATED_BOARD, responseDto));
  }


  @PutMapping("/{boardId}")
  public ResponseEntity<BaseResponse<BoardResponseDto>> updateBoard(@PathVariable Long boardId,
      @RequestBody BoardRequestDto requestDto,
      @AuthUser Member authMember) {
    BoardResponseDto responseDto = boardService.updateBoard(boardId, requestDto, authMember.getId());

    return ResponseEntity.status(HttpStatus.OK)
        .body(BaseResponse.of(ResponseCode.UPDATE_BOARD, responseDto));
  }

  @DeleteMapping("/{boardId}")
  public ResponseEntity<BaseResponse<String>> deleteBoard(@PathVariable Long boardId,
      @AuthUser Member authMember) {

    boardService.deleteBoard(boardId, authMember.getId());

    return ResponseEntity.status(HttpStatus.OK)
        .body(BaseResponse.of(ResponseCode.DELETE_BOARD, ""));
  }

  @PostMapping("/{boardId}")
  public ResponseEntity<BaseResponse<String>> inviteMember(@PathVariable Long boardId,
      @RequestBody
      InviteMemberDto inviteMemberDto) {
    boardService.inviteMember(boardId, inviteMemberDto);

    return ResponseEntity.status(HttpStatus.OK)
        .body(BaseResponse.of(ResponseCode.INVITE_USER, ""));
  }

  @GetMapping("/{boardId}")
  public ResponseEntity<BaseResponse<List<BoardListResponseDto>>> getBoard(
      @PathVariable Long boardId) {
    List<BoardListResponseDto> responseDtoList = boardService.getBoard(boardId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(BaseResponse.of(ResponseCode.GET_BOARD, responseDtoList));
  }
}
