package com.example.securityapp.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.ToString;

@ToString
public class MemberDto extends User {
    private String email;
    private String password;
    private String nickname;
    private List<String> roles;

    public MemberDto(String email, String password, String nickname, List<String> roleNames){
        super(email
        , password
        , roleNames.stream().map(roleName-> new SimpleGrantedAuthority("ROLE_"+roleName)).collect(Collectors.toList()));
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.roles = roleNames;
        
    }
    
    public Map<String, Object> getClaims(){
        Map<String, Object> map = new HashMap<>();

        map.put("email", email);
        map.put("password", password);
        map.put("nickname", nickname);
        map.put("roleNames", roles);
        return map;
    }
}
