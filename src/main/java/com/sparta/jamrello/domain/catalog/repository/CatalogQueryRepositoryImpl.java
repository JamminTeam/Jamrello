package com.sparta.jamrello.domain.catalog.repository;

import static com.sparta.jamrello.domain.catalog.repository.entity.QCatalog.catalog;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class CatalogQueryRepositoryImpl implements CatalogQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CatalogQueryRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void updateCatalogPosition(Long catalogId, Long changePos) {

        queryFactory.update(catalog)
                .set(catalog.position, changePos)
                .where(catalog.id.eq(catalogId))
                .execute();
        em.flush();
        em.clear();
    }

    @Override
    public void decreasePositionBeforeUpdate(Long boardId, Long currentPos, Long changedPos) {

        queryFactory.update(catalog)
                .where(catalog.board.id.eq(boardId)
                        .and(catalog.position.between(currentPos + 1, changedPos)))
                .set(catalog.position, catalog.position.subtract(1))
                .execute();
        em.flush();
        em.clear();
    }

    @Override
    public void increasePositionBeforeUpdate(Long boardId, Long currentPos, Long changedPos) {

        queryFactory.update(catalog)
                .where(catalog.board.id.eq(boardId)
                        .and(catalog.position.between(changedPos, currentPos - 1)))
                .set(catalog.position, catalog.position.add(1))
                .execute();
        em.flush();
        em.clear();
    }

    @Override
    public void decreasePositionBeforeDelete(Long boardId, Long currentPos) {

        queryFactory.update(catalog)
                .where(catalog.board.id.eq(boardId).and(catalog.position.gt(currentPos)))
                .set(catalog.position, catalog.position.subtract(1))
                .execute();
        em.flush();
        em.clear();
    }
}
