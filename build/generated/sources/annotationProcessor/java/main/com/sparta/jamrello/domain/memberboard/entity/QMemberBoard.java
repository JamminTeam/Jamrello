package com.sparta.jamrello.domain.memberboard.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberBoard is a Querydsl query type for MemberBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberBoard extends EntityPathBase<MemberBoard> {

    private static final long serialVersionUID = 624109803L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberBoard memberBoard = new QMemberBoard("memberBoard");

    public final com.sparta.jamrello.domain.board.entity.QBoard board;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.sparta.jamrello.domain.member.repository.entity.QMember member;

    public final EnumPath<MemberBoardRoleEnum> role = createEnum("role", MemberBoardRoleEnum.class);

    public QMemberBoard(String variable) {
        this(MemberBoard.class, forVariable(variable), INITS);
    }

    public QMemberBoard(Path<? extends MemberBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberBoard(PathMetadata metadata, PathInits inits) {
        this(MemberBoard.class, metadata, inits);
    }

    public QMemberBoard(Class<? extends MemberBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.sparta.jamrello.domain.board.entity.QBoard(forProperty("board")) : null;
        this.member = inits.isInitialized("member") ? new com.sparta.jamrello.domain.member.repository.entity.QMember(forProperty("member")) : null;
    }

}

