package com.example.securityapp.utils;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import com.example.securityapp.exception.CustomJwtException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

/*  
    1. JWT 토큰 발행 (헤더, 페이로드(Claims), 시그니처(비밀키 사용 / 30자 이상))
    2. JWT 토큰 검증
*/

public class JwtUtil {
    private static String key = "adslifjiasdlhfilsdahflasehlirweirwei21412";

    // claims - 사용자 정보 / min - 토큰만료시간
    public static String generateToken(Map<String, Object> claims, int min){
        SecretKey sKey = null;
        try {
            // HMAC-SHA 알고리즘
            sKey = Keys.hmacShaKeyFor(JwtUtil.key.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        String jwtStr = Jwts.builder()
                                .setHeader(Map.of("typ", "JWT"))
                                .setClaims(claims) // 페이로드 (클레임 - 사용자정보)
                                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant())) // 발행시간 (toInstant - 타임존명 제거 UTC로 반환)
                                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())) // 만료시간
                                .signWith(sKey) // 시그니처 (비밀키)
                                .compact();

        return jwtStr;
    }

    // 토큰 검증
    public static Map<String, Object> validationToken(String token){

        Map<String, Object> claims = null;
        try {
           SecretKey sKey = Keys.hmacShaKeyFor(JwtUtil.key.getBytes("UTF-8"));
           
           // 파싱 및 검증
           claims = Jwts.parserBuilder()
                            .setSigningKey(sKey)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
            
        } catch (MalformedJwtException e) { // JWT 문자열 형식 잘못됨
            throw new CustomJwtException("Malformed");
        }
        catch (ExpiredJwtException e) { // JWT 인증이 만료가 되었을때
            throw new CustomJwtException("Expired");
        }
        catch (InvalidClaimException e) { // 클레임 정보가 유효하지 않을때
            throw new CustomJwtException("Invalid");
        }
        catch (JwtException e) {
            throw new CustomJwtException("Jwt");
        }
        catch(Exception e){
            throw new CustomJwtException("Error");
        }

        return claims;
    }
}
