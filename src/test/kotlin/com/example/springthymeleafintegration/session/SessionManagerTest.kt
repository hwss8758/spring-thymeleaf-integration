package com.example.springthymeleafintegration.session

import com.example.springthymeleafintegration.domain.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

internal class SessionManagerTest {

    private val sessionManager = SessionManager()

    @Test
    fun sessionTest() {

        //given

        //session create
        val response = MockHttpServletResponse()
        val member = Member()
        sessionManager.createSession(member, response)

        //요청에 응답 쿠키 저장
        val request = MockHttpServletRequest()
        request.setCookies(*response.cookies)

        //when & then

        //session search
        val result = sessionManager.getSession(request)
        assertThat(result).isEqualTo(member)

        //session expire
        sessionManager.expire(request)
        val expired = sessionManager.getSession(request)
        assertThat(expired).isNull()
    }
}