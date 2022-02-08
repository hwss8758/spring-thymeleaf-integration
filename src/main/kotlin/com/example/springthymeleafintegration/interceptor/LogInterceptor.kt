package com.example.springthymeleafintegration.interceptor

import mu.KotlinLogging
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.UUID
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LogInterceptor : HandlerInterceptor {

    companion object {
        const val LOG_ID = "logId"
    }

    private val logger = KotlinLogging.logger {}

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val requestURI = request.requestURI
        val uuid = UUID.randomUUID().toString()

        request.setAttribute(LOG_ID, uuid)

        /**
         * @RequestMapping: HandlerMethod
         * 정적 리소스: ResourceHttpRequestHandler
         */
        if (handler is HandlerMethod) {
            // 호출할 컨트롤러 메소드의 모든 정보가 들어있다.
            val hm = handler as HandlerMethod
        }

        logger.info { "REQUEST [$uuid][$requestURI][$handler]" }

        return true
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        logger.info { "postHandle [$modelAndView]" }
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val requestURI = request.requestURI
        val logId = request.getAttribute(LOG_ID).toString()

        logger.info { "RESPONSE [$logId][$requestURI]" }

        if (ex != null) {
            logger.error { "afterCompletion error!! [$ex]" }
        }

    }
}