package com.example.securityapp.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        
        log.error("exception : {} ", exception);

        response.setContentType("application/json; charset=UTF-8");
        Gson gson = new Gson();
        String jsonStr = gson.toJson(Map.of("result", "ERROR_LOGIN"));
        PrintWriter pWriter = response.getWriter();
        pWriter.println(jsonStr);
        pWriter.close();
    }
}
