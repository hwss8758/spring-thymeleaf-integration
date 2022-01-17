package com.example.springthymeleafintegration.message

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import java.util.*

@SpringBootTest
class MessageSourceTest {

    @Autowired
    lateinit var ms: MessageSource

    @Test
    fun helloMessage() {
        val result = ms.getMessage("hello", null, Locale.KOREA)
        assertEquals(result, "안녕")

    }

    @Test
    fun notFoundMessageCode() {
        assertThrows(NoSuchMessageException::class.java) {
            ms.getMessage("no_code", null, Locale.KOREA)
        }
    }

    @Test
    fun notFoundMessageCodeDefaultMessage() {
        val result = ms.getMessage("no_code", null, "기본메세지", Locale.KOREA)
        assertThat(result).isEqualTo("기본메세지")
    }

    @Test
    fun argumentMessage() {
        val result = ms.getMessage("hello.name", arrayOf("Spring"), Locale.KOREA)
        assertThat(result).isEqualTo("안녕 Spring")
    }
}