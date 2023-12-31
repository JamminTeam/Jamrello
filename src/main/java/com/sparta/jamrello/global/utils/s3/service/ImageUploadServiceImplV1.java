package com.sparta.jamrello.global.utils.s3.service;

import com.sparta.jamrello.domain.board.entity.Board;
import com.sparta.jamrello.domain.board.repository.BoardRepository;
import com.sparta.jamrello.domain.comment.repository.CommentRepository;
import com.sparta.jamrello.domain.comment.repository.entity.Comment;
import com.sparta.jamrello.domain.member.repository.MemberRepository;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import com.sparta.jamrello.domain.memberboard.repository.MemberBoardRepository;
import com.sparta.jamrello.global.utils.s3.dto.response.BoardImageResponseDto;
import com.sparta.jamrello.global.exception.BisException;
import com.sparta.jamrello.global.exception.ErrorCode;
import com.sparta.jamrello.global.utils.s3.dto.response.CommentImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageUploadServiceImplV1 implements ImageUploadService {


    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final MemberBoardRepository memberBoardRepository;
    private final CommentRepository commentRepository;
    private final S3Uploader s3Uploader;
    @Value("${cloud.aws.s3.bucket}")
    private String s3Bucket;

    @Transactional
    public BoardImageResponseDto uploadBoardFile(MultipartFile file, Long boardId,
        Long authMember) {
        Board requestBoard = findByBoardId(boardId);
        Member member = findByAuthMemberId(authMember);
        findByMemberIdAndBoardId(member, requestBoard);

        String fileName = "board" + Long.toString(boardId);
        String fileUrl = s3Uploader.uploadFile(file, fileName);

        requestBoard.updateBoardImageUrl(fileUrl, "#FFFFFF");
        boardRepository.save(requestBoard);

        return new BoardImageResponseDto(fileUrl);
    }

    @Transactional
    public void deleteBoardBackgroundImage(Long boardId, Long authMember) {
        Board requestBoard = findByBoardId(boardId);
        Member member = findByAuthMemberId(authMember);
        findByMemberIdAndBoardId(member, requestBoard);

        String fileName = "board" + Long.toString(boardId);
        s3Uploader.deleteFile(fileName);

        requestBoard.removeBoardImageUrl();
        boardRepository.save(requestBoard);
    }


    private Board findByBoardId(Long boardId) {
        Board requestBoard = boardRepository.findById(boardId).orElseThrow(
            () -> new BisException(ErrorCode.NOT_FOUND_BOARD)
        );
        return requestBoard;
    }

    private Member findByAuthMemberId(Long authMember) {
        Member member = memberRepository.findById(authMember).orElseThrow(
            () -> new BisException(ErrorCode.NOT_FOUND_MEMBER)
        );
        return member;
    }

    private void findByMemberIdAndBoardId(Member member, Board requestBoard) {
        memberBoardRepository.findByMemberAndBoard(member, requestBoard).orElseThrow(
            () -> new BisException(ErrorCode.YOUR_NOT_INVITED_BOARD)
        );
    }

    @Override
    @Transactional
    public CommentImageResponseDto uploadCommentFile(MultipartFile file, Long commentId,
        Long memberId) {
        Comment comment = findByCommentId(commentId);

        String fileName = "comment" + Long.toString(commentId);
        String fileUrl = s3Uploader.uploadFile(file, fileName);

        comment.updateImageUrl(fileUrl);
        commentRepository.save(comment);

        return new CommentImageResponseDto(fileUrl);
    }

    @Transactional
    public void deleteCommentImage(Long commentId, Long memberId) {
        Comment comment = findByCommentId(commentId);

        String fileName = "comment" + Long.toString(commentId);
        s3Uploader.deleteFile(fileName);

        comment.removeImageUrl(fileName);
        commentRepository.save(comment);
    }

    private Comment findByCommentId(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new BisException(ErrorCode.NOT_FOUND_COMMENT));
    }
}
