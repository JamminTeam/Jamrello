package com.sparta.jamrello.global.utils.s3.service;

import com.sparta.jamrello.domain.board.entity.Boards;
import com.sparta.jamrello.domain.board.repository.BoardRepository;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.domain.memberBoard.repository.MemberBoardRepository;
import com.sparta.jamrello.global.utils.s3.dto.response.BoardImageResponseDto;
import com.sparta.jamrello.global.exception.BisException;
import com.sparta.jamrello.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageUploadServiceImplV1 implements ImageUploadService{


  @Value("${cloud.aws.s3.bucket}")
  private String s3Bucket;

  private final MemberRepository memberRepository;
  private final BoardRepository boardRepository;
  private final MemberBoardRepository memberBoardRepository;
  private final S3Uploader s3Uploader;

  @Transactional
  public BoardImageResponseDto uploadFile(MultipartFile file, Long boardId,
      Long authMember) {
    Boards requestBoard = boardRepository.findById(boardId).orElseThrow(
        () -> new BisException(ErrorCode.NOT_FOUND_BOARD)
    );
    Member member = memberRepository.findById(authMember).orElseThrow(
        () -> new BisException(ErrorCode.NOT_FOUND_MEMBER)
    );
    memberBoardRepository.findByMemberAndBoard(member, requestBoard).orElseThrow(
        () -> new BisException(ErrorCode.YOUR_NOT_INVITED_BOARD)
    );

    String fileName = Long.toString(boardId);
    String fileUrl = s3Uploader.uploadFile(file, fileName);


    requestBoard.updateBoardImageUrl(fileUrl, "#FFFFFF");
    boardRepository.save(requestBoard);

    return new BoardImageResponseDto(fileUrl);
  }

  @Transactional
  public void deleteBackgroundImage(Long boardId, Long authMember) {
    Boards requestBoard = boardRepository.findById(boardId).orElseThrow(
        () -> new BisException(ErrorCode.NOT_FOUND_BOARD)
    );
    Member member = memberRepository.findById(authMember).orElseThrow(
        () -> new BisException(ErrorCode.NOT_FOUND_MEMBER)
    );
    memberBoardRepository.findByMemberAndBoard(member, requestBoard).orElseThrow(
        () -> new BisException(ErrorCode.YOUR_NOT_INVITED_BOARD)
    );

    String fileName = Long.toString(boardId);
    s3Uploader.deleteFile(fileName);

    requestBoard.removeBoardImageUrl();
    boardRepository.save(requestBoard);
  }

}
