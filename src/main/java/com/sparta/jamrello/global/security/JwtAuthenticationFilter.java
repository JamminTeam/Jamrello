package com.sparta.jamrello.global.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.jamrello.domain.member.dto.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(),
                    LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.username(),
                            requestDto.password(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        String nickname = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
//        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String accessToken = jwtUtil.createAccessToken(nickname);
        String refreshToken = "";

//        try {
//            //  Http 로그인 URL 요청시 토큰저장소 조회 **
//            RefreshToken refreshTokenIns = jwtUtil.getTokenDBByNickname(nickname);
//            refreshToken = refreshTokenIns.getRefreshToken();
//
//        } catch (NullPointerException e) {
//            refreshToken = jwtUtil.createRefreshToken(nickname, role);
//            // RefreshToken DB에 저장
//            jwtUtil.saveRefreshJwtToDB(refreshToken, nickname);
//        }

        // RefreshToken 쿠키에 저장
        jwtUtil.addJwtToCookie(refreshToken, response);

        // AccessToken 헤더에 저장
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);

        response.setStatus(200);
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.println("200 : Ok");
        writer.println("로그인 성공!");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        log.info("로그인 실패");

        response.setStatus(403);
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.println(" 403 : Forbidden");
        writer.println("닉네임 또는 패스워드를 확인해주세요.");

    }
}