package com.example.securityapp.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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
        Gson gson = new Gson();
        String jsonStr= gson.toJson(Map.of("result", "로그인에 성공하였습니다."));
        response.setContentType("application/json; charset=UTF-8");
        
        log.info("authentication : {} ", authentication);
        log.info("principal : {} ",authentication.getPrincipal());

        PrintWriter pw = response.getWriter(); // PrintStream(바이트) PrintWriter(문자)
        pw.println(jsonStr);
        pw.close();
    }
}
