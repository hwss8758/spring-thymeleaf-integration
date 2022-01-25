package com.example.springthymeleafintegration.controller

import com.example.springthymeleafintegration.domain.Member
import com.example.springthymeleafintegration.repository.MemberRepository
import com.example.springthymeleafintegration.session.SessionManager
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import javax.servlet.http.HttpServletRequest

@Controller
class HomeController(
    private val memberRepository: MemberRepository,
    private val sessionManager: SessionManager
) {

    //    @GetMapping("/")
    @Deprecated("사용하지 않는 함수")
    fun home(): String = "home"

    @Deprecated("")
    //@GetMapping("/")
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

    @GetMapping("/")
    fun homeLoginV2(
        request: HttpServletRequest,
        model: Model
    ): String {
        val member = sessionManager.getSession(request) as Member? ?: return "home"
        model.addAttribute("member", member)
        return "loginHome"
    }
}