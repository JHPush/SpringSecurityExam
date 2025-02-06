package com.example.securityapp.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.securityapp.security.filter.JwtCheckFilter;
import com.example.securityapp.security.handler.ApiLoginFailureHandler;
import com.example.securityapp.security.handler.ApiLoginSuccessHandler;
import com.example.securityapp.security.handler.CustomExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity // 활성화
@EnableMethodSecurity // 인가와 관련 (메소드 관련 인가 처리하려면 필요)
public class CustomSecurityConfig {
    /*
        1. CSRF 비활성화
        2. CORS 설정 - 동일출처 정책 / 다른 도메인에서 서버의 서비스를 사용할 수 있도록 허용 (포트번호까지 오리진 ex.. localhost:5136 까지 오리진이다, 내부적으로 서버에 요청을 보낼떄 오리진이 다름 -> 동일출처 정책으로 도메인이 다를때 차단을 시킴)
        3. 세션 비활성화
        4. 패스워드 암호화
    */

    // 스프링 시큐리티의 시큐리티필터체인을 설정하여 스프링 부트 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.info("-------------------------- CustomSecurity.filterChanin");
        // CORS 설정
        http.cors((httpSecurityConfigure)->{
            httpSecurityConfigure.configurationSource(corsConfigurationSource());
        });

        // CSRF 비활성 - (CSRF, 사이트간 요청 위조) 웹에서 사용하는 공격 유형 중 하나 - 공격자가 인증된 사용자 세션 정보 탈취
        // JWT 사용시 CSRF 사전 차단 가능하다
        http.csrf(config->{config.disable();});

        // 세션 비활성화 - 사용자 정보를 서버에서 관리하지 않도록 쿠키로 관리해야함
        http.sessionManagement((sesisonConfig)->{
            sesisonConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        // 폼 기반 로그인 인증
        http.formLogin(config->{
            config.loginPage("/api/v1/members/login"); // 로그인 요청 처리할 엔드포인트 지정
            config.successHandler(new ApiLoginSuccessHandler());
            config.failureHandler(new ApiLoginFailureHandler());
        });

        http.addFilterBefore(new JwtCheckFilter(), UsernamePasswordAuthenticationFilter.class); // beforeFilter 이전에 필터 등록하는거임
        // jwtCheckFilter 에서 이미 인증정보를 컨텍스트에 바인딩 시킨 경우 Username~필터는 그냥 스킵됨 
        
        http.exceptionHandling(config->{
            config.accessDeniedHandler(new CustomExceptionHandler());
        });

        return http.build();
    }

    

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder(); // 패스워드 암호화 알고리즘
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:5173")); // 리액트 포트

        // OPTIONS - (Preflight) 사전에 서버에 접속하여 요청을 허용하는지 확인하는데 이때 OPTIONS 사용
        configuration.setAllowedMethods(Arrays.asList("GET","PUT","POST","HEAD","DELETE", "OPTIONS"));
        
        // 요청 헤더 - 해당 헤더만 허용한다 (Authorization - JWT 인증 토큰 포함 / Cache-Control - 캐시관련)
        configuration.setAllowedHeaders(Arrays.asList("Authorization","Cache-Control","Content-Type"));

        // 클라이언트 인증정보(쿠키, Autho헤더, TLS클라 인증 등)를 요청헤더에 포함시킬 수 있도록 허용
        configuration.setAllowCredentials(true);

        // 모든 경로에 대한 CORS 설정을 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
