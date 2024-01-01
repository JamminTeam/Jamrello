package com.sparta.jamrello.domain.board.service;

import com.sparta.jamrello.domain.board.dto.request.BoardRequestDto;
import com.sparta.jamrello.domain.board.dto.request.InviteMemberDto;
import com.sparta.jamrello.domain.board.dto.response.BoardListResponseDto;
import com.sparta.jamrello.domain.board.dto.response.BoardResponseDto;
import com.sparta.jamrello.domain.board.dto.response.getFromCardListDto;
import com.sparta.jamrello.domain.board.entity.Board;
import com.sparta.jamrello.domain.board.repository.BoardRepository;
import com.sparta.jamrello.domain.card.repository.entity.Card;
import com.sparta.jamrello.domain.catalog.repository.entity.Catalog;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.domain.memberboard.entity.MemberBoard;
import com.sparta.jamrello.domain.memberboard.entity.MemberBoardRoleEnum;
import com.sparta.jamrello.domain.memberboard.repository.MemberBoardRepository;
import com.sparta.jamrello.global.exception.BisException;
import com.sparta.jamrello.global.exception.ErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardServiceImplV1 implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final MemberBoardRepository memberBoardRepository;

    @Override
    public BoardResponseDto createBoard(BoardRequestDto requestDto, Member authMember) {
        Board board = Board.fromRequestDto(requestDto);
        boardRepository.save(board);

        MemberBoard memberBoard = MemberBoard.createMemberBoard(authMember, board,
            MemberBoardRoleEnum.ADMIN);
        memberBoardRepository.save(memberBoard);

        return new BoardResponseDto(board.getTitle(), board.getBackgroundColor());
    }

    @Transactional
    @Override
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto,
        Long authMemberId) {
        Board board = findByBoardId(boardId);
        Member member = findByMemberId(authMemberId);
        checkPermissionfromMemberAndBoard(member, board);

        board.update(requestDto);

        return new BoardResponseDto(board.getTitle(), board.getBackgroundColor());
    }

    @Transactional
    @Override
    public void deleteBoard(Long boardId, Long authMemberId) {
        Board board = findByBoardId(boardId);
        Member member = findByMemberId(authMemberId);
        MemberBoard memberBoard = checkPermissionfromMemberAndBoard(member, board);

        memberBoardRepository.delete(memberBoard);
        boardRepository.delete(board);
    }

    @Transactional
    @Override
    public void inviteMember(Long boardId, InviteMemberDto inviteMemberDto) {
        // 이메일 존재 여부
        Member member = memberRepository.findByEmail(inviteMemberDto.email()).orElseThrow(
            () -> new BisException(ErrorCode.NOT_FOUND_MEMBER)
        );

        // 보드 존재 여부
        Board board = findByBoardId(boardId);

        // 멤버가 해당 보드에 있는지 확인
        Optional<MemberBoard> getMemberBoardInfo = memberBoardRepository.findByMemberAndBoard(
            member,
            board);

        MemberBoard memberBoard = getMemberBoardInfo
            .map(getMemberBoard -> {
                getMemberBoard.updateRole(MemberBoardRoleEnum.INVITED_MEMBER);
                return getMemberBoard;
            })
            .orElseGet(() -> new MemberBoard(member, board, MemberBoardRoleEnum.INVITED_MEMBER));

        // 저장
        memberBoardRepository.save(memberBoard);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BoardListResponseDto> getBoard(Long boardId) {
        Board board = findByBoardId(boardId);
        Board getBoard = boardRepository.findByIdWithCatalogListAndCardList(boardId).orElseThrow(
            () -> new BisException(ErrorCode.NOT_FOUND_BOARD)
        );

        List<Catalog> catalogList = board.getCatalogList();

        List<BoardListResponseDto> responseDtoList = catalogList.stream()
            .map(catalog -> {
                List<Card> cardList = catalog.getCardList();
                List<getFromCardListDto> getCardList = cardList.stream()
                    .map(card -> new getFromCardListDto(
                        card.getTitle(),
                        card.getImageUrl(),
                        card.getBackgroundColor(),
                        card.getCommentList().size(),
                        card.getCardCollaboratorList().size()
                    )).toList();
                return new BoardListResponseDto(catalog.getId(), catalog.getTitle(), getCardList);
            })
            .toList();
        return responseDtoList;
    }

    private Board findByBoardId(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
            () -> new BisException(ErrorCode.NOT_FOUND_BOARD)
        );

        return board;
    }

    // 인증된 유저가 실제로 존재하는지 확인
    private Member findByMemberId(Long authMemberId) {
        Member member = memberRepository.findById(authMemberId).orElseThrow(
            () -> new BisException(ErrorCode.NOT_FOUND_MEMBER)
        );

        return member;
    }

    // 인증된 유저가 보드에 있는지 확인
    private MemberBoard checkPermissionfromMemberAndBoard(Member member, Board board) {
        MemberBoard memberBoard = memberBoardRepository.findByMemberAndBoard(member, board)
            .orElseThrow(
                () -> new BisException(ErrorCode.YOUR_NOT_INVITED_BOARD)
            );

        return memberBoard;
    }
}
