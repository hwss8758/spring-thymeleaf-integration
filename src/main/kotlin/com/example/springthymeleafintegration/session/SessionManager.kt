package com.example.springthymeleafintegration.session

import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SessionManager {

    companion object {
        private const val SESSION_COOKIE_NAME = "mySessionId"
    }

    private val sessionStore: MutableMap<String, Any> = ConcurrentHashMap()

    /**
     * session create
     */
    fun createSession(value: Any, response: HttpServletResponse) {
        println("SessionManager.createSession")
        val sessionId = UUID.randomUUID().toString()
        sessionStore[sessionId] = value

        val mySessionCookie = Cookie(SESSION_COOKIE_NAME, sessionId)
        response.addCookie(mySessionCookie)
    }

    fun getSession(request: HttpServletRequest): Any? {
        println("SessionManager.getSession")
        val sessionCookie = findCookie(request, SESSION_COOKIE_NAME)
        return if (sessionCookie == null) null
        else sessionStore[sessionCookie.value]
    }

    private fun findCookie(request: HttpServletRequest, cookieName: String): Cookie? {
        println("SessionManager.findCookie")
        val cookies = request.cookies
        return if (cookies.isEmpty()) null
        else cookies.firstOrNull { cookie -> cookie.name.equals(cookieName) }
    }

    fun expire(request: HttpServletRequest) {
        println("SessionManager.expire")
        val sessionCookie = findCookie(request, SESSION_COOKIE_NAME)
        if (sessionCookie != null) sessionStore.remove(sessionCookie.value)
    }
}