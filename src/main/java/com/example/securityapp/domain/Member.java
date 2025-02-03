package com.example.securityapp.domain;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_member")
@Slf4j
@ToString
public class Member {
    @Id
    private String email;
    private String password;
    private String nickname;
    
    @Builder.Default
    @ElementCollection // 별도 테이블이 생성됨
    @CollectionTable(name = "member_role", joinColumns = @JoinColumn(name = "email", referencedColumnName = "email")) 
    @Enumerated(EnumType.STRING)
    private List<MemberRole> roles = new ArrayList<>();

    // 아래는 비즈니스 메소드들

    // 권한 추가
    public void addRoles(MemberRole role){
        if(roles.contains(role)){
            log.warn("Already include role in user! : {}", role);
            return;
        }
        this.roles.add(role);
    }

    public void changePassword(String newPass){
        this.password = newPass;
    }
    public void changeNickname(String newNick){
        this.nickname = newNick;
    }

}
