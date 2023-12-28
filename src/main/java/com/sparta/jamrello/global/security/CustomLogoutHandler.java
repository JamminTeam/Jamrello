package com.sparta.jamrello.global.security;

import com.sparta.jamrello.global.constant.ResponseCode;
import com.sparta.jamrello.global.security.jwt.JwtUtil;
import com.sparta.jamrello.global.security.jwt.RefreshTokenRepository;
import com.sparta.jamrello.global.utils.RedisService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomLogoutHandler implements LogoutHandler {
  private final RefreshTokenRepository refreshTokenRepository;

  private final RedisService redisService;

  private final JwtUtil jwtUtil;

  public CustomLogoutHandler(RefreshTokenRepository refreshTokenRepository,
      RedisService redisService, JwtUtil jwtUtil) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.redisService = redisService;
    this.jwtUtil = jwtUtil;
  }



  @Override
  @Transactional
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    String accessToken = jwtUtil.getJwtFromHeader(request);
    Claims member = jwtUtil.getUserInfoFromToken(accessToken);
    String username = member.getSubject();

    redisService.setValues(username, accessToken, Duration.ofMinutes(30));   // AccessToken 만료시간
    refreshTokenRepository.deleteByKeyUsername(username);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(ResponseCode.LOGOUT.getHttpStatus());
    response.setCharacterEncoding("utf-8");
    try {
      response.getWriter().write("HttpStatus" + ":" + ResponseCode.LOGOUT.getHttpStatus() + "\n" + ResponseCode.LOGOUT.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
