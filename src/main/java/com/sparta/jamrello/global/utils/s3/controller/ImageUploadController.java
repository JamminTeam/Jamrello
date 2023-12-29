package com.sparta.jamrello.global.utils.s3.controller;

import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.global.annotation.AuthUser;
import com.sparta.jamrello.global.constant.ResponseCode;
import com.sparta.jamrello.global.dto.BaseResponse;
import com.sparta.jamrello.global.utils.s3.dto.response.BoardImageResponseDto;
import com.sparta.jamrello.global.utils.s3.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageUploadController {

  private final ImageUploadService imageUploadService;

  @PutMapping("/boards/image/{boardId}")
  public ResponseEntity<BaseResponse<BoardImageResponseDto>> backgroundImageUpload(
      @PathVariable Long boardId,
      @RequestParam(value = "file") MultipartFile file, @AuthUser Member authMember
  ) {
    BoardImageResponseDto responseDto = imageUploadService.uploadFile(file, boardId
        , authMember.getId());

    return ResponseEntity.status(HttpStatus.OK)
        .body(BaseResponse.of(ResponseCode.UPDATE_IMAMGE, responseDto));
  }

  @DeleteMapping("/boards/image/{boardId}")
  public ResponseEntity<BaseResponse<String>> deleteBackgroundImage(@PathVariable Long boardId,
      @AuthUser Member authMember) {

    imageUploadService.deleteBackgroundImage(boardId, authMember.getId());

    return ResponseEntity.status(HttpStatus.OK)
        .body(BaseResponse.of(ResponseCode.DELETE_IMAMGE, ""));
  }
}
