package com.sparta.jamrello.domain.comment.repository;

import com.sparta.jamrello.domain.comment.repository.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
