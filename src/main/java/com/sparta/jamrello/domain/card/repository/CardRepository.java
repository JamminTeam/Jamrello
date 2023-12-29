package com.sparta.jamrello.domain.card.repository;

import com.sparta.jamrello.domain.card.repository.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

}
