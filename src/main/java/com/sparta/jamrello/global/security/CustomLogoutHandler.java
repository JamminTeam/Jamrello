package com.sparta.jamrello.global.security;

import com.sparta.jamrello.global.constant.ResponseCode;
import com.sparta.jamrello.global.security.jwt.JwtUtil;
import com.sparta.jamrello.global.security.jwt.RefreshTokenRepository;
import com.sparta.jamrello.global.utils.RedisService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "logout 핸들러")
@Service
public class CustomLogoutHandler implements LogoutHandler {
  private final RefreshTokenRepository refreshTokenRepository;

  private final RedisService redisService;

  private final JwtUtil jwtUtil;

  public CustomLogoutHandler(RefreshTokenRepository refreshTokenRepository, RedisService redisService, JwtUtil jwtUtil) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.redisService = redisService;
    this.jwtUtil = jwtUtil;
  }



  @Override
  @Transactional
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    String accessToken = jwtUtil.getJwtFromHeader(request);
    if (!jwtUtil.validateToken(accessToken)) {
      try {
        log.error("유효하지않은 AccesToken");
        response.setStatus(401);
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.println(" 401 : UNAUTHORIZED");
        writer.println("유효하지 않은 토큰입니다.");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return;
    }
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
