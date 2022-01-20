package com.example.springthymeleafintegration.validation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.validation.DefaultMessageCodesResolver
import org.springframework.validation.MessageCodesResolver

class MessageCodesResolverTest {

    private val codesResolver: MessageCodesResolver = DefaultMessageCodesResolver()

    @Test
    fun messageCodesResolverObject() {
        val messageCodes = codesResolver.resolveMessageCodes("required", "item")
        assertThat(messageCodes).containsExactly("required.item", "required")
    }

    @Test
    fun messageCodesResolverField() {
        val messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String::class.java)
        assertThat(messageCodes).containsExactly(
            "required.item.itemName",
            "required.itemName",
            "required.java.lang.String",
            "required"
        )
    }
}