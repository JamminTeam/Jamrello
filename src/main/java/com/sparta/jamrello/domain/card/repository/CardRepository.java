package com.sparta.jamrello.domain.card.repository;

import com.sparta.jamrello.domain.card.repository.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryQuery {

}
