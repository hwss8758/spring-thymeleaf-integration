package com.example.springthymeleafintegration.controller

import com.example.springthymeleafintegration.domain.Member
import com.example.springthymeleafintegration.dto.LoginForm
import com.example.springthymeleafintegration.service.LoginService
import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Controller
class LoginController(private val loginService: LoginService) {

    private val logger = KotlinLogging.logger {}

    @GetMapping("/login")
    fun loginForm(@ModelAttribute form: LoginForm): String {
        return "login/loginForm"
    }

    @PostMapping("/login")
    fun login(
        @Validated @ModelAttribute form: LoginForm,
        bindingResult: BindingResult,
        response: HttpServletResponse
    ): String {
        if (bindingResult.hasErrors()) return "login/loginForm"

        val loginMember = loginService.login(form.loginId!!, form.password!!)

        logger.info { "login? $loginMember" }

        if (loginMember == null) {
            bindingResult.reject("loginFail", "id or password 가 맞지 않습니다.")
            return "login/loginForm"
        }

        setCookie(loginMember, response)

        return "redirect:/"
    }

    private fun setCookie(
        loginMember: Member,
        response: HttpServletResponse
    ) {
        val cookie = Cookie("memberId", loginMember.id.toString())
        response.addCookie(cookie)
    }

    @PostMapping("/logout")
    fun logout(response: HttpServletResponse): String {
        expireCookie(response, "memberId")
        return "redirect:/"
    }

    private fun expireCookie(response: HttpServletResponse, cookieName: String) {
        val cookie = Cookie(cookieName, null)
        cookie.maxAge = 0
        response.addCookie(cookie)
    }
}