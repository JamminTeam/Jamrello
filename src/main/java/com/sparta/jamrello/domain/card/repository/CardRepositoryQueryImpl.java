package com.sparta.jamrello.domain.card.repository;

import static com.sparta.jamrello.domain.card.repository.entity.QCard.card;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class CardRepositoryQueryImpl implements CardRepositoryQuery {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CardRepositoryQueryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void updateCardPosition(Long cardId, Long changePos) {
        
        queryFactory.update(card)
            .set(card.position, changePos)
            .where(card.id.eq(cardId))
            .execute();
        em.flush();
        em.clear();
    }

    @Override
    public void decreasePositionBeforeUpdate(Long catalogId, Long currentPos, Long changedPos) {

        queryFactory.update(card)
            .where(card.catalog.id.eq(catalogId)
                .and(card.position.between(currentPos + 1, changedPos)))
            .set(card.position, card.position.subtract(1))
            .execute();
        em.flush();
        em.clear();
    }

    @Override
    public void increasePositionBeforeUpdate(Long catalogId, Long currentPos, Long changedPos) {

        queryFactory.update(card)
            .where(card.catalog.id.eq(catalogId)
                .and(card.position.between(changedPos, currentPos - 1)))
            .set(card.position, card.position.add(1))
            .execute();
        em.flush();
        em.clear();
    }
}
