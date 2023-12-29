package com.sparta.jamrello.domain.memberBoard.repository;

import static com.sparta.jamrello.domain.memberBoard.entity.QMemberBoard.memberBoard;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.jamrello.domain.memberBoard.entity.MemberBoard;
import jakarta.persistence.EntityManager;
import java.util.Optional;


public class MemberBoardQueryRepositoryImpl implements MemberBoardQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberBoardQueryRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<MemberBoard> findByMemberIdAndBoardId(Long memberId, Long boardId) {

        MemberBoard result = queryFactory
                .selectFrom(memberBoard)
                .where(memberBoard.member.id.eq(memberId).and(memberBoard.board.id.eq(boardId)))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
