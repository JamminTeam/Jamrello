package com.sparta.jamrello.domain.catalog.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.sparta.jamrello.domain.catalog.dto.CatalogPositionRequestDto;
import com.sparta.jamrello.domain.catalog.dto.CatalogRequestDto;
import com.sparta.jamrello.domain.catalog.dto.CatalogResponseDto;
import com.sparta.jamrello.domain.catalog.service.CatalogService;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.global.annotation.AuthUser;
import com.sparta.jamrello.global.constant.ResponseCode;
import com.sparta.jamrello.global.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    @PostMapping
    public ResponseEntity<BaseResponse<CatalogResponseDto>> createCatalog(
            @PathVariable Long boardId, @AuthUser Member member,
            @RequestBody CatalogRequestDto requestDto) {

        CatalogResponseDto responseDto = catalogService.createCatalog(boardId, member.getId(),
                requestDto);

        return ResponseEntity.status(CREATED)
                .body(BaseResponse.of(ResponseCode.CREATED_CATALOG, responseDto));
    }

    @PatchMapping("/{catalogId}")
    public ResponseEntity<BaseResponse<CatalogResponseDto>> updateCatalogTitle(
            @PathVariable Long boardId, @PathVariable Long catalogId, @AuthUser Member member,
            @RequestBody CatalogRequestDto requestDto) {

        CatalogResponseDto responseDto = catalogService.updateCatalogTitle(boardId, member.getId(),
                catalogId, requestDto);

        return ResponseEntity.status(OK)
                .body(BaseResponse.of(ResponseCode.UPDATE_CATALOG, responseDto));
    }

    @PatchMapping("/{catalogId}/delete")
    public ResponseEntity<BaseResponse<String>> updateCatalogStatus(@PathVariable Long boardId,
            @PathVariable Long catalogId, @AuthUser Member member) {
        catalogService.updateCatalogStatus(boardId, member.getId(), catalogId);

        return ResponseEntity.status(OK).body(BaseResponse.of(ResponseCode.KEEP_CATALOG, ""));
    }

    @DeleteMapping("/{catalogId}/delete")
    public ResponseEntity<BaseResponse<String>> deleteCatalog(@PathVariable Long boardId,
            @PathVariable Long catalogId, @AuthUser Member member) {
        catalogService.deleteCatalog(boardId, member.getId(), catalogId);

        return ResponseEntity.status(OK).body(BaseResponse.of(ResponseCode.DELETE_CATALOG, ""));
    }

    @PatchMapping("/{catalogId}/pos")
    public ResponseEntity<BaseResponse<String>> updateCatalogPos(@PathVariable Long boardId,
            @PathVariable Long catalogId, @RequestBody CatalogPositionRequestDto requestDto,
            @AuthUser Member member) {
        catalogService.updateCatalogPos(boardId, member.getId(), catalogId, requestDto);

        return ResponseEntity.status(OK)
                .body(BaseResponse.of(ResponseCode.MOVE_CATALOG_POSITION, ""));
    }
}
