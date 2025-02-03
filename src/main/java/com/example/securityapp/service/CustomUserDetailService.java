package com.example.securityapp.service;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.securityapp.domain.Member;
import com.example.securityapp.dto.MemberDto;
import com.example.securityapp.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("user : {}", username);
        Member user = memberRepository.getWithRoles(username);

        if (user == null) {
            throw new UsernameNotFoundException(username + "Not Found!!");
        }

        MemberDto memberDto = new MemberDto(
                user.getEmail(), user.getPassword(), user.getNickname(),
                user.getRoles().stream().map(role -> role.name()).collect(Collectors.toList()));
        return memberDto;
    }
}
