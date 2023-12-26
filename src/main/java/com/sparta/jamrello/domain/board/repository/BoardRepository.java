package com.sparta.jamrello.domain.board.repository;

import com.sparta.jamrello.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
