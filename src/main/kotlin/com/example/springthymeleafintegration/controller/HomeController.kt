package com.example.springthymeleafintegration.controller

import com.example.springthymeleafintegration.repository.MemberRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController(private val memberRepository: MemberRepository) {

    //    @GetMapping("/")
    @Deprecated("사용하지 않는 함수")
    fun home(): String = "home"

    @GetMapping("/")
    fun homeLogin(
        @CookieValue(name = "memberId", required = false) memberId: Long?,
        model: Model
    ): String {
        if (memberId == null) return "home"

        val loginMember = memberRepository.findById(memberId)
        if (loginMember.isEmpty) return "home"

        model.addAttribute("member", loginMember.get())
        return "loginHome"

    }
}