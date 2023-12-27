package com.sparta.jamrello.global.security.jwt;

import com.sparta.jamrello.global.time.TimeStamp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@Table(name = "refreshTokens")
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long refreshTokenId;

  @Column(nullable = false)
  private String refreshToken;

  @Column(nullable = false)
  private String keyUsername;
}
