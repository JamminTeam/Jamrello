package com.sparta.jamrello.domain.memberBoard.repository;

import com.sparta.jamrello.domain.memberBoard.entity.MemberBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBoardRepository extends JpaRepository<MemberBoard, Long>,
        MemberBoardQueryRepository {

}
