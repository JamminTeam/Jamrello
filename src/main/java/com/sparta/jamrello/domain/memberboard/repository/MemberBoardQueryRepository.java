package com.sparta.jamrello.domain.memberBoard.repository;

import com.sparta.jamrello.domain.memberBoard.entity.MemberBoard;
import java.util.Optional;

public interface MemberBoardQueryRepository {

    Optional<MemberBoard> findByMemberIdAndBoardId(Long memberId, Long boardId);
}
