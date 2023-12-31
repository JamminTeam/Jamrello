package com.sparta.jamrello.domain.card.repository;

public interface CardRepositoryQuery {

    void updateCardPosition(Long cardId, Long changePos);

    void decreasePositionBeforeUpdate(Long catalogId, Long currentPos, Long changedPos);

    void increasePositionBeforeUpdate(Long catalogId, Long currentPos, Long changedPos);
}
