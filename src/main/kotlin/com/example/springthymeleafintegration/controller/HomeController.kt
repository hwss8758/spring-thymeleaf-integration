package com.example.springthymeleafintegration.controller

import com.example.springthymeleafintegration.SessionConst
import com.example.springthymeleafintegration.domain.Member
import com.example.springthymeleafintegration.repository.MemberRepository
import com.example.springthymeleafintegration.session.SessionManager
import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.SessionAttribute
import javax.servlet.http.HttpServletRequest

@Controller
class HomeController(
    private val memberRepository: MemberRepository,
    private val sessionManager: SessionManager
) {

    private val logger = KotlinLogging.logger {}

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

    @Deprecated("")
//    @GetMapping("/")
    fun homeLoginV2(
        request: HttpServletRequest,
        model: Model
    ): String {
        val member = sessionManager.getSession(request) as Member? ?: return "home"
        model.addAttribute("member", member)
        return "loginHome"
    }

    @Deprecated("")
//    @GetMapping("/")
    fun homeLoginV3(
        request: HttpServletRequest,
        model: Model
    ): String {
        println("HomeController.homeLoginV3")
        val session = request.getSession(false) ?: return "home"
        val loginMember = session.getAttribute(SessionConst.LOGIN_MEMBER) as Member?

        return if (loginMember == null) {
            "home"
        } else {
            model.addAttribute("member", loginMember)
            "loginHome"
        }
    }

    @GetMapping("/")
    fun homeLoginV4(
        @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) loginMember: Member?,
        model: Model
    ): String {
        println("HomeController.homeLoginV4")
        return if (loginMember == null) {
            "home"
        } else {
            model.addAttribute("member", loginMember)
            "loginHome"
        }
    }
}