package com.sparta.jamrello.global.utils.s3.service;

import com.sparta.jamrello.global.utils.s3.dto.response.BoardImageResponseDto;
import com.sparta.jamrello.global.utils.s3.dto.response.CommentImageResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {

    /**
     * S3 이미지 업로드
     *
     * @Param file
     * @Param memberId
     */
    BoardImageResponseDto uploadBoardFile(MultipartFile file, Long boardId, Long authMember);

    /**
     * S3 이미지 삭제
     *
     * @param boardId
     * @param member
     */
    void deleteBoardBackgroundImage(Long boardId, Long member);

    CommentImageResponseDto uploadCommentFile(MultipartFile file, Long commentId, Long authMember);

    void deleteCommentImage(Long commentId, Long memberId);
}
