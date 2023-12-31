package com.sparta.jamrello.domain.cardCollaborators.repository;

import com.sparta.jamrello.domain.cardCollaborators.repository.entity.CardCollaborator;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardCollaboratorRepository extends JpaRepository<CardCollaborator, Long> {

    Optional<CardCollaborator> findByCardIdAndMemberId(Long cardId, Long memberId);
}
