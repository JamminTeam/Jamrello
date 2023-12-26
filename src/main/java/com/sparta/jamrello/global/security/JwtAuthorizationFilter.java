package com.sparta.jamrello.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.jamrello.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;


@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService,
            ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
            FilterChain filterChain) throws ServletException, IOException {
//
//        String accessTokenValue = jwtUtil.getJwtFromHeader(req);
//        if (StringUtils.hasText(accessTokenValue)) {
//            log.info(accessTokenValue);
//
//            try {
//                if (!jwtUtil.validateToken(accessTokenValue)) {
//                    log.error("유효하지않은 AccesToken");
//                    setResponse(res, ErrorCode.ACCESS_DENIED);
//                    return;
//                }
//
//            } catch (ExpiredJwtException e) {
//                // 만료된 accessToken 일경우 accessToken 재발급
//                // 쿠키에서 리프레시 토큰가져와서 유효성 검사후  발급
//                String refreshToken = jwtUtil.getTokenFromRequest(req);
//                log.info(refreshToken);
//                if (refreshToken == null) {
//                    log.error("쿠키에 RereshToken이 존재하지 않습니다.");
//                    setResponse(res, ErrorCode.UNKNOWN_ERROR_NOT_EXIST_REFRESHTOKEN);
//                    return;
//                }
//
//                // refrshToken 검증
//                try {
//                    if (!jwtUtil.validateToken(refreshToken)) {
//                        log.error("유효하지않은 RefreshToken");
//                        setResponse(res,ErrorCode.ACCESS_DENIED);
//                        return;
//                    }
//                } catch (ExpiredJwtException exception) {
//                    log.error("만료된 RefreshToken");
//                    setResponse(res,ErrorCode.EXPIRED_TOKEN);
//                    return;
//                }
//
//                // refreshToken DB조회
//                if (!jwtUtil.checkTokenDBByToken(refreshToken)) {
//                    log.error("DB에 해당 RefreshToken이 존재하지 않습니다.");
//                    setResponse(res,ErrorCode.UNKNOWN_ERROR_NOT_EXIST_REFRESHTOKEN);
//                    return;
//                }
//
//                // accessToken 재발급
//                Claims user = jwtUtil.getUserInfoFromToken(refreshToken);
//                String accessToken = jwtUtil.createAccessToken(user.getSubject(), UserRoleEnum.valueOf(user.get(AUTHORIZATION_KEY).toString()));
//
//                // AccessToken 헤더에 저장
//                res.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
//
//                res.setStatus(200);
//                res.setCharacterEncoding("utf-8");
//                PrintWriter writer = res.getWriter();
//                writer.println("AccessToken이 재발급되었습니다. 다시 시도 해주세요.");
//
//                return;
//            }
//
//            // 정상 동작일때
//            Claims info = jwtUtil.getUserInfoFromToken(accessTokenValue);
//
//            try {
//                setAuthentication(info.getSubject());
//            } catch (Exception e) {
//                log.error(e.getMessage());
//                setResponse(res, ErrorCode.INVALID_VALUE);
//                return;
//            }
//        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(errorCode.getHttpStatus().value());
//        response.setCharacterEncoding("utf-8");
//        ErrorResponse errorResponse = new ErrorResponse(errorCode.getHttpStatus(), errorCode.getMessage());

//        try {
//            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

}