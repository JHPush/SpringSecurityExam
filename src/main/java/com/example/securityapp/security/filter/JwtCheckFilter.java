package com.example.securityapp.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.securityapp.dto.MemberDto;
import com.example.securityapp.utils.JwtUtil;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtCheckFilter extends OncePerRequestFilter { // 요청할때마다 필터를 처리한다

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        log.info("path : {} ", path);
        
        // pre flight 요청 (OPTIONS) 
        if (request.getMethod().equals("OPTIONS") || path.startsWith("/api/v1/members/"))
            return true;

        return false; // false doFilterInternal 실행 / true doFilterInternal 실행X
    }

    // Jwt 토큰 검증
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        log.info("header : {} ",header);
        try {
            String accessToken = header.split(" ")[1]; 
            Map<String, Object> claims = JwtUtil.validationToken(accessToken);  

            log.info("email : {} ", claims.get("email"));
            log.info("nickname : {} ", claims.get("nickname"));
            log.info("password : {} ", claims.get("password"));
            log.info("rolenames : {} ", claims.get("roleNames"));
            
            String email = (String)claims.get("email");
            String password = (String)claims.get("password");
            String nickname = (String)claims.get("nickname");
            List<String> roles = (List<String>)claims.get("roleNames");


            MemberDto memberDto = new MemberDto(email,password,nickname,roles);
            log.warn(memberDto.toString());

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(memberDto, memberDto.getPassword(), memberDto.getAuthorities());
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            filterChain.doFilter(request, response); // 다음 필터 수행, 다음 필터 없을때는 디스패처 서블릿에 전달 
            
        } catch (Exception e) {
            log.error("Error : {}", e.getMessage());
            Gson gson = new Gson();
            String jsonStr = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter pWriter = response.getWriter();
            pWriter.println(jsonStr);
            pWriter.close();
        }


    }
}
