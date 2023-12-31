package com.sparta.jamrello.domain.board.repository;

import com.sparta.jamrello.domain.board.entity.Board;
import java.util.Optional;
import org.hibernate.graph.SubGraph;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

  @Query("SELECT b FROM Board b LEFT JOIN FETCH b.catalogList WHERE b.id = :boardId")
  Optional<Board> findBoardWithCatalog(Long boardId);

//  @Query("SELECT b FROM Board b LEFT JOIN b.catalogList c ON b.id = c.board.id LEFT JOIN c.cardList cl ON c.id = cl.catalog.id WHERE b.id = :boardId")
//  Optional<Board> findByIdWithCatalogListAndCardList(Long boardId);
}