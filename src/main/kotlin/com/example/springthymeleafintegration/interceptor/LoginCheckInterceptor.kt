package com.example.springthymeleafintegration.interceptor

import com.example.springthymeleafintegration.SessionConst
import mu.KotlinLogging
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginCheckInterceptor : HandlerInterceptor {

    private val logger = KotlinLogging.logger {}

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val requestURI = request.requestURI
        val requestURL = request.requestURL

        logger.info { "requestURI[$requestURI], requestURL[$requestURL]" }

        val session = request.getSession(false)

        return if (session?.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            logger.info { "미인증 사용자 요청" }
            response.sendRedirect("/login?redirectURL=$requestURI")
            false
        } else true
    }
}