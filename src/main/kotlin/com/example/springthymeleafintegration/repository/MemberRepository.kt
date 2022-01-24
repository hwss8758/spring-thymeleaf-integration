package com.example.springthymeleafintegration.repository

import com.example.springthymeleafintegration.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {

    fun findByLoginId(loginId: String): Member?
}