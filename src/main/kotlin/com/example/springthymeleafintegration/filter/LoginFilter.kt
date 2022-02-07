package com.example.springthymeleafintegration.filter

import mu.KotlinLogging
import java.util.*
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

class LoginFilter : Filter {

    private val logger = KotlinLogging.logger {}

    override fun init(filterConfig: FilterConfig?) {
        logger.info { "LoginFilter.init" }
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest
        val requestURI = httpServletRequest.requestURI
        val uuid = UUID.randomUUID().toString()

        try {
            logger.info { "REQUEST [$uuid] [$requestURI]" }
            chain?.doFilter(request, response)
        } catch (e: Exception) {
            throw e
        } finally {
            logger.info { "RESPONSE [$uuid] [$requestURI]" }
        }
    }

    override fun destroy() {
        logger.info { "LoginFilter.destroy" }
    }
}