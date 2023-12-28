package com.sparta.jamrello.domain.catalog.service;

import com.sparta.jamrello.domain.catalog.dto.CatalogPositionRequestDto;
import com.sparta.jamrello.domain.catalog.dto.CatalogRequestDto;
import com.sparta.jamrello.domain.catalog.dto.CatalogResponseDto;

public interface CatalogService {

    CatalogResponseDto createCatalog(Long boardId, Long memberId, CatalogRequestDto requestDto);

    CatalogResponseDto updateCatalogTitle(Long boardId, Long memberId, Long catalogId,
            CatalogRequestDto requestDto);

    void updateCatalogStatus(Long boardId, Long memberId, Long catalogId);

    void deleteCatalog(Long boardId, Long memberId, Long catalogId);

    void updateCatalogPos(Long boardId, Long memberId, Long catalogId,
            CatalogPositionRequestDto requestDto);
}
