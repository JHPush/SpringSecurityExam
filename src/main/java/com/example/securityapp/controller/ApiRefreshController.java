package com.example.securityapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securityapp.exception.CustomJwtException;
import com.example.securityapp.utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ApiRefreshController {
    @GetMapping("/members/refresh")
    public ResponseEntity<Map<String,String>> getMethodName(@RequestHeader("Authorization") String authHeader
                                                            , @RequestParam("refreshToken") String refreshToken) {
        if(refreshToken == null)
            throw new CustomJwtException("NULL_REFRESH");
        
        if(authHeader == null || authHeader.length() < 7)
            throw new CustomJwtException("INVALID_AUTH");

        String accessToken = authHeader.split(" ")[1];

        // 1. accessToken 이 만료되지 않는 경우
        if(!checkExpiredToken(accessToken))
            return new ResponseEntity<>(Map.of("accessToken", accessToken, "refreshToken", refreshToken), HttpStatus.OK);
        
        // 2. accessToken 만료시
        Map<String,Object> claims = JwtUtil.validationToken(refreshToken);
        String newAccessToken = JwtUtil.generateToken(claims, 10);
        /*
         *  .setIssuedAt -> iat
            .setExpiration -> exp
         */
        log.info("isaTime : {} ", claims.get("iat"));
        log.info("expTime : {} ",  claims.get("exp"));
        
        String newRefreshToken = checkRefreshTime((Integer)claims.get("exp"))? JwtUtil.generateToken(claims, 60*24) : refreshToken;

        return new ResponseEntity<>(Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken), HttpStatus.OK);
    }

    // 엑세스 토큰이 만료되었는지
    public boolean checkExpiredToken(String accessToken){
        try {  
            JwtUtil.validationToken(accessToken);
        } catch (Exception e) {
            return (e.getMessage().equals("Expired"))? true:false; 
        }
        return false;
    }
    
    // 리프레시 토큰 만료 시간이 한시간 미만인지
    public boolean checkRefreshTime(Integer expire){
        // expire 를 날짜(Date) 변환
        Date exeDate = new Date((long)expire *60 * 1000); // 밀리세컨드 변환
        long gap = exeDate.getTime() - System.currentTimeMillis(); 
        long leftMin = gap/(60*1000); // 분으로 변환
        
        return leftMin < 60;
    }

}
