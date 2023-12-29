package com.sparta.jamrello.global.security.jwt;

import com.sparta.jamrello.global.time.TimeStamp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "refreshTokens")
@NoArgsConstructor
public class RefreshToken extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long refreshTokenId;

  @Column(nullable = false)
  private String refreshToken;

  @Column(nullable = false)
  private String keyUsername;

  @Builder
  public RefreshToken (String refreshToken, String keyUsername) {
    this.refreshToken = refreshToken;
    this.keyUsername = keyUsername;
  }

  public static RefreshToken createRefreshToken (String refreshToken, String keyUsername) {
    return RefreshToken.builder()
        .refreshToken(refreshToken)
        .keyUsername(keyUsername)
        .build();
  }
}
