package com.example.springthymeleafintegration.argumentresolver

import com.example.springthymeleafintegration.SessionConst
import com.example.springthymeleafintegration.domain.Member
import mu.KotlinLogging
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

class LoginMemberArgumentResolver : HandlerMethodArgumentResolver {

    private val logger = KotlinLogging.logger {}

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        logger.info { "supportsParameter 실행" }

        val hasParameterAnnotation = parameter.hasParameterAnnotation(Login::class.java)
        val hasMemberType = Member::class.java.isAssignableFrom(parameter.parameterType)

        return hasParameterAnnotation && hasMemberType
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        logger.info { "resolveArgument 실행" }

        val request = webRequest.nativeRequest as HttpServletRequest
        return request.getSession(false)?.getAttribute(SessionConst.LOGIN_MEMBER)

    }
}