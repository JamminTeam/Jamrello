package com.sparta.jamrello.domain.member.repository;

import com.sparta.jamrello.domain.member.repository.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

  boolean existsUserByEmail(String email);

  boolean existsUserByUsername(String username);

  boolean existsUserByNickname(String Nickname);
}
