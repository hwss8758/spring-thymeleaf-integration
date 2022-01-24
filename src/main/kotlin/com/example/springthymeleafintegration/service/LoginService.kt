package com.example.springthymeleafintegration.service

import com.example.springthymeleafintegration.domain.Member
import com.example.springthymeleafintegration.repository.MemberRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class LoginService(private val repository: MemberRepository) {

    private val logger = KotlinLogging.logger {}

    fun login(loginId: String, password: String): Member? {
        val member = repository.findByLoginId(loginId)

        return if (member?.checkPassword(password) == true) {
            logger.info { "member: $member" }
            member
        }
        else null
    }
}