package com.sparta.jamrello.domain.memberBoard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBoardRepository extends JpaRepository<MemberBoard, Long>, MemberBoardQueryRepository {
}
