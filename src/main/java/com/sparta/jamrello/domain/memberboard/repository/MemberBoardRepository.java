package com.sparta.jamrello.domain.memberBoard.repository;

import com.sparta.jamrello.domain.board.entity.Board;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.domain.memberBoard.entity.MemberBoard;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBoardRepository extends JpaRepository<MemberBoard, Long>,
    MemberBoardQueryRepository {

    Optional<MemberBoard> findByMemberAndBoard(Member member, Board board);
}
