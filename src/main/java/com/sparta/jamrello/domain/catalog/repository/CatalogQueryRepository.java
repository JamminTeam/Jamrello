package com.sparta.jamrello.domain.catalog.repository;

public interface CatalogQueryRepository {

    void updateCatalogPosition(Long catalogId, Long changePos);

    void decreasePositionBeforeUpdate(Long boardId, Long currentPos, Long changedPos);

    void increasePositionBeforeUpdate(Long boardId, Long currentPos, Long changedPos);

    void decreasePositionBeforeDelete(Long boardId, Long currentPos);
}
