package com.sprta.jamrello.global.entity;

public enum MemberBoardRoleEnum {
    ADMIN(Role.ADMIN),
    INVITED_MEMBER(Role.INVITED_MEMBER),
    NOT_INVITED_MEMBER(Role.NOT_INVITED_MEMBER);

    private final String role;

    MemberBoardRoleEnum(String role) {
        this.role = role;
    }

    public static class Role {

        public static final String ADMIN = "ADMIN";
        public static final String INVITED_MEMBER = "INVITED_MEMBER";
        public static final String NOT_INVITED_MEMBER = "NOT_INVITED_MEMBER";
    }
}
