package com.sparta.jamrello.domain.board.repository;

import com.sparta.jamrello.domain.board.entity.Board;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.catalogList WHERE b.id = :boardId")
    Optional<Board> findBoardWithCatalog(Long boardId);
}
