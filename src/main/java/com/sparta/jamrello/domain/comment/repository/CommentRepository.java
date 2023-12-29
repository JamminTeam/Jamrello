package com.sparta.jamrello.domain.comment.repository;

import com.sparta.jamrello.domain.comment.repository.entity.Comment;
import com.sparta.jamrello.domain.member.repository.entity.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c.member FROM Comment c WHERE c.id = :commentId")
    Optional<Member> findMemberByCommentId(@Param("commentId") Long commentId);

    @Query("SELECT c FROM Comment c")
    Page<Comment> findAllCommentsWithPagination(Pageable pageable);

}
