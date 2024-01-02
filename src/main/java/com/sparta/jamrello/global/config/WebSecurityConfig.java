package com.sparta.jamrello.global.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.jamrello.global.security.CustomLogoutHandler;
import com.sparta.jamrello.global.security.UserDetailsServiceImpl;
import com.sparta.jamrello.global.security.jwt.JwtAuthenticationFilter;
import com.sparta.jamrello.global.security.jwt.JwtAuthorizationFilter;
import com.sparta.jamrello.global.security.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    private final ObjectMapper objectMapper;

    private final CustomLogoutHandler logoutHandler;

    public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService,
        AuthenticationConfiguration authenticationConfiguration, ObjectMapper objectMapper,
        CustomLogoutHandler logoutHandler) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
        this.objectMapper = objectMapper;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }

    // jwt 인증필터
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    // jwt 인가필터
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService, objectMapper);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        http.formLogin((formLogin) ->
            formLogin
                .loginPage("/jamrello/loginPage").permitAll()
                .defaultSuccessUrl("/jamrello/main")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(
                    new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                            System.out.println("authentication : " + authentication.getName());
                            response.sendRedirect("/jamrello/main"); // 인증이 성공한 후에는 root로 이동
                        }
                    }
                )
                .failureHandler(
                    new AuthenticationFailureHandler() {
                        @Override
                        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                            throws IOException, ServletException, IOException {
                            System.out.println("exception : " + exception.getMessage());
                            response.sendRedirect("/login");
                        }
                    }
                )
        );


        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                .requestMatchers("/api/members/signup", "/api/members/email", "/api/members/login", "/jamrello/signup")
                .permitAll()// 회원가입, 로그인요청 인증허가
                .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        http.logout()
            .logoutUrl("/api/members/logout")
            .deleteCookies("refreshtoken")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));
//                        .permitAll();

        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
