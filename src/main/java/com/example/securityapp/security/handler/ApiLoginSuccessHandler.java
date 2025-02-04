package com.example.securityapp.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.securityapp.dto.MemberDto;
import com.example.securityapp.utils.JwtUtil;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiLoginSuccessHandler implements AuthenticationSuccessHandler{
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        log.info("authentication : {} ", authentication);
        log.info("principal : {} ",authentication.getPrincipal());

        MemberDto memberDto = (MemberDto)authentication.getPrincipal();
        Map<String,Object> claims = memberDto.getClaims();
        String accessToken = JwtUtil.generateToken(claims, 10);
        String refreshToken = JwtUtil.generateToken(claims, 60*24);

        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(claims);
        response.setContentType("application/json; charset=UTF-8");
        

        PrintWriter pw = response.getWriter(); // PrintStream(바이트) PrintWriter(문자)
        pw.println(jsonStr);
        pw.close();
    }
}
