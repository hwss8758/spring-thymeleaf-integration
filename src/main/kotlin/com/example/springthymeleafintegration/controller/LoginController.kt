package com.example.springthymeleafintegration.controller

import com.example.springthymeleafintegration.SessionConst
import com.example.springthymeleafintegration.domain.Member
import com.example.springthymeleafintegration.dto.LoginForm
import com.example.springthymeleafintegration.service.LoginService
import com.example.springthymeleafintegration.session.SessionManager
import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class LoginController(
    private val loginService: LoginService,
    private val sessionManager: SessionManager
) {

    private val logger = KotlinLogging.logger {}

    @GetMapping("/login")
    fun loginForm(@ModelAttribute form: LoginForm): String {
        println("LoginController.loginForm")
        return "login/loginForm"
    }

    //    @PostMapping("/login")
    @Deprecated("사용하지 않음")
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

    @Deprecated("")
//    @PostMapping("/login")
    fun loginV2(
        @Validated @ModelAttribute form: LoginForm,
        bindingResult: BindingResult,
        response: HttpServletResponse
    ): String {
        println("LoginController.loginV2")
        if (bindingResult.hasErrors()) return "login/loginForm"

        val loginMember = loginService.login(form.loginId!!, form.password!!)

        logger.info { "login? $loginMember" }

        if (loginMember == null) {
            bindingResult.reject("loginFail", "id or password 가 맞지 않습니다.")
            return "login/loginForm"
        }

        sessionManager.createSession(loginMember, response)

        return "redirect:/"
    }

    @PostMapping("/login")
    fun loginV3(
        @Validated @ModelAttribute form: LoginForm,
        bindingResult: BindingResult,
        request: HttpServletRequest
    ): String {
        println("LoginController.loginV3")
        if (bindingResult.hasErrors()) return "login/loginForm"

        val loginMember = loginService.login(form.loginId!!, form.password!!)

        logger.info { "login? $loginMember" }

        if (loginMember == null) {
            bindingResult.reject("loginFail", "id or password 가 맞지 않습니다.")
            return "login/loginForm"
        }

        val session = request.session // session이 있으면 session 반환, 없으면 신규 session 생성
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember) // session에 로그인한 회원정보 보관

        return "redirect:/"
    }

    private fun setCookie(
        loginMember: Member,
        response: HttpServletResponse
    ) {
        println("LoginController.setCookie")
        val cookie = Cookie("memberId", loginMember.id.toString())
        response.addCookie(cookie)
    }

    //@PostMapping("/logout")
    @Deprecated("사용하지 않음")
    fun logout(response: HttpServletResponse): String {
        println("LoginController.logout")
        expireCookie(response, "memberId")
        return "redirect:/"
    }

    @Deprecated("")
//    @PostMapping("/logout")
    fun logoutV2(request: HttpServletRequest): String {
        println("LoginController.logoutV2")
        sessionManager.expire(request)
        return "redirect:/"
    }

    @PostMapping("/logout")
    fun logoutV3(request: HttpServletRequest): String {
        println("LoginController.logoutV3")
        request.getSession(false)?.invalidate()
        return "redirect:/"
    }

    private fun expireCookie(response: HttpServletResponse, cookieName: String) {
        println("LoginController.expireCookie")
        val cookie = Cookie(cookieName, null)
        cookie.maxAge = 0
        response.addCookie(cookie)
    }
}