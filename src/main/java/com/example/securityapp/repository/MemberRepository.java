package com.example.securityapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.securityapp.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>{ // Entity, Pk
    // 해당하는 이메일의 사용자정보를 조회
    @Query("SELECT m FROM Member m JOIN FETCH m.roles WHERE m.email = :email")
    Member getWithRoles(@Param("email") String email);

}
