package com.sparta.jamrello.domain.memberboard.repository;

import com.sparta.jamrello.domain.memberboard.entity.MemberBoard;
import java.util.Optional;

public interface MemberBoardQueryRepository {

    Optional<MemberBoard> findByMemberIdAndBoardId(Long memberId, Long boardId);
}
