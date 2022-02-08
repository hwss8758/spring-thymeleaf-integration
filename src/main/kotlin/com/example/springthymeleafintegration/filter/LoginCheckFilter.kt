package com.example.springthymeleafintegration.filter

import com.example.springthymeleafintegration.SessionConst
import mu.KotlinLogging
import org.springframework.util.PatternMatchUtils
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginCheckFilter : Filter {

    private val logger = KotlinLogging.logger {}

    companion object {
        private val whiteList = arrayOf("/", "/members/add", "/login", "/logout", "/css/*")
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest
        val httpServletResponse = response as HttpServletResponse

        val requestURI = httpServletRequest.requestURI

        try {
            logger.info { "인증 체크 필터 시작 [$requestURI]" }

            if (loginCheckFilter(requestURI, httpServletRequest, httpServletResponse)) return

            chain?.doFilter(request, response)

        } catch (e: Exception) {
            throw e
        } finally {
            logger.info { "인증 체크 필터 종료 [$requestURI]" }
        }
    }

    private fun loginCheckFilter(
        requestURI: String,
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse
    ): Boolean {
        if (isLoginCheckPath(requestURI)) {

            logger.info { "인증 체크 로직 실행 [$requestURI]" }

            val session = httpServletRequest.getSession(false)

            if (session?.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                logger.info { "미인증 사용자 요청 [$requestURI]" }
                httpServletResponse.sendRedirect("login?redirectURL=$requestURI")
                return true
            }
        }
        return false
    }

    private fun isLoginCheckPath(requestURI: String) = !PatternMatchUtils.simpleMatch(whiteList, requestURI)
}