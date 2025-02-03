package com.example.securityapp.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.example.securityapp.domain.Member;
import com.example.securityapp.domain.MemberRole;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
@Slf4j
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test(){
        assertNotNull(memberRepository);
    }

    @Test
    @Rollback(value = false)
    public void testSave(){
        for(int i = 0; i<10; i++){
            Member member = Member.builder().email("qnrn" + i + "@gmail.com").password(passwordEncoder.encode("1111")).nickname("push"+i).build();
            member.addRoles(MemberRole.USER);
            if(i >= 5)
                member.addRoles(MemberRole.MANAGER);
            if(i>=9)
                member.addRoles(MemberRole.ADMIN);
            memberRepository.save(member);
        }
    }

    @Test
    public void testJoined(){
        // given
    
        // when
        Member member = memberRepository.getWithRoles("qnrn2@gmail.com");

        // then
        assertNotNull(member);
        log.info("find member : {} ", member);

    }
}
